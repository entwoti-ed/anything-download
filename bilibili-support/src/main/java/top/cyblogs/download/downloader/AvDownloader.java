package top.cyblogs.download.downloader;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import top.cyblogs.api.AvApi;
import top.cyblogs.data.BiliBiliData;
import top.cyblogs.data.DownloadList;
import top.cyblogs.data.SettingsData;
import top.cyblogs.exception.BadRequestException;
import top.cyblogs.model.DownloadItem;
import top.cyblogs.model.TempDownloadItem;
import top.cyblogs.model.enums.DownloadType;
import top.cyblogs.model.enums.ServiceType;
import top.cyblogs.util.FileUtils;
import top.cyblogs.utils.BiliBiliUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AvDownloader {


    public static String download(String url) {

        String listId = IdUtil.fastSimpleUUID();
        List<TempDownloadItem> list = new ArrayList<>();

        String playId = BiliBiliUtils.getPlayId(url);
        // 获取视频初始化信息
        JsonNode initialState = AvApi.getInitialState(playId);
        // 获取分P
        JsonNode pages = initialState.findValue("pages");

        if (pages.size() == 0) {
            throw new BadRequestException("无法获取视频数据...");
        }

        pages.forEach(x -> {

            String title = "";

            // 默认标题
            title += FileUtils.getPrettyFileName(initialState.findValue("videoData").findValue("title").asText());
            if (pages.size() > 1) {
                // 分P标题
                title += File.separator + String.format("P%s %s",
                        x.findValue("page").asText(),
                        FileUtils.getPrettyFileName(x.findValue("part").asText()));
            }

            String cid = x.findValue("cid").asText();
            // 获取视频的播放地址
            JsonNode videoUrl = AvApi.getVideoUrl(playId, cid);
            // 如果视频为dash
            JsonNode dash = videoUrl.findValue("dash");

            DownloadItem videoStatus = new DownloadItem();
            videoStatus.setSource(BiliBiliData.SOURCE);
            videoStatus.setDownloadType(DownloadType.VIDEO);
            File targetFile = new File(SettingsData.path + title + ".mp4");


            if (dash != null) {
                String[] dashUrl = getDashUrl(dash);

                list.add(
                        TempDownloadItem.init(dashUrl, targetFile, ServiceType.SEPERATE, BiliBiliData.header(), videoStatus)
                );
                return;
            }
            // 如果视频为durl
            JsonNode durl = videoUrl.findValue("durl");
            if (durl != null) {
                String[] durlUrl = getDurlUrl(durl);
                list.add(
                        TempDownloadItem.init(durlUrl, targetFile, ServiceType.SEGMENT, BiliBiliData.header(), videoStatus)
                );
            }
        });

        DownloadList.tempMap.put(listId, list);
        System.err.println(JSONUtil.toJsonStr(DownloadList.tempMap));
        return listId;
    }

    /**
     * 下载Dash视频
     */
    private static String[] getDashUrl(JsonNode dash) {
        JsonNode video = dash.findValue("video");
        Stream<JsonNode> videoStream = StreamSupport.stream(video.spliterator(), false);
        String videoUrl = videoStream.max(Comparator.comparingInt(x -> x.get("id").asInt()))
                .orElse(video.get(0)).findValue("baseUrl").asText()
                .replaceAll("http://", "https://");

        JsonNode audio = dash.findValue("audio");
        Stream<JsonNode> audioStream = StreamSupport.stream(audio.spliterator(), false);
        String audioUrl = audioStream.max(Comparator.comparingInt(x -> x.get("id").asInt()))
                .orElse(video.get(0)).findValue("baseUrl").asText()
                .replaceAll("http://", "https://");

        return new String[]{videoUrl, audioUrl};
    }

    /**
     * 下载Durl视频
     */
    private static String[] getDurlUrl(JsonNode durl) {
        String[] flvUrls = new String[durl.size()];
        for (int i = 0; i < durl.size(); i++) {
            flvUrls[i] = durl.get(i).findValue("url").asText().replaceAll("http://", "https://");
        }
        return flvUrls;
    }
}
