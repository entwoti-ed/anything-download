package top.cyblogs.download.downloader;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import top.cyblogs.api.BvApi;
import top.cyblogs.data.BiliBiliData;
import top.cyblogs.data.DownloadList;
import top.cyblogs.data.SettingsData;
import top.cyblogs.model.DownloadItem;
import top.cyblogs.model.TempDownloadItem;
import top.cyblogs.model.enums.DownloadType;
import top.cyblogs.model.enums.ServiceType;
import top.cyblogs.util.FileUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public class BvDownloader {

    public static void download(String url) {

        log.info("当前解析地址为BV ==> " + url);

        try {
            url = new URL(url).getPath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // 获取视频初始化信息
        AtomicReference<BvApi> bvApi = new AtomicReference<>(new BvApi(url));
        JsonNode initialState = bvApi.get().getInitialState();

        // 获取分P
        JsonNode pages = initialState.findValue("pages");

        if (pages.size() == 0) {
            return;
        }

        String bigTitle = FileUtils.getPrettyFileName(initialState.findValue("videoData").findValue("title").asText());

        String finalUrl = url;
        pages.forEach(x -> {

            String title = bigTitle;

            // 默认标题
            if (pages.size() > 1) {

                int page = x.findValue("page").asInt();
                // 分P标题
                title += File.separator + String.format("P%s %s", page, FileUtils.getPrettyFileName(x.findValue("part").asText()));
                if (page > 1) {
                    String path = finalUrl + "?p=" + page;
                    log.info("正在解析: {}", path);
                    bvApi.set(new BvApi(path));

                }
            }

            // 获取视频的播放地址
            JsonNode videoUrl = bvApi.get().getVideoUrl();
            // 如果视频为dash
            JsonNode dash = videoUrl.findValue("dash");

            DownloadItem videoStatus = new DownloadItem();
            videoStatus.setSource(BiliBiliData.SOURCE);
            videoStatus.setDownloadType(DownloadType.VIDEO);
            File targetFile = new File(SettingsData.path + title + ".mp4");

            if (dash != null) {
                String[] dashUrl = getDashUrl(dash);

                DownloadList.tempList.add(
                        TempDownloadItem.init(dashUrl, targetFile, ServiceType.SEPERATE, BiliBiliData.header(), videoStatus)
                );
                return;
            }
            // 如果视频为durl
            JsonNode durl = videoUrl.findValue("durl");
            if (durl != null) {
                String[] durlUrl = getDurlUrl(durl);
                DownloadList.tempList.add(
                        TempDownloadItem.init(durlUrl, targetFile, ServiceType.SEGMENT, BiliBiliData.header(), videoStatus)
                );
            }
        });

        System.err.println(JSONUtil.toJsonStr(DownloadList.tempList));
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
