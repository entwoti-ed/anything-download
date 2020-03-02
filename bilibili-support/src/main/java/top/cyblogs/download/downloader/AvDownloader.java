package top.cyblogs.download.downloader;

import com.fasterxml.jackson.databind.JsonNode;
import top.cyblogs.api.AvApi;
import top.cyblogs.data.BiliBiliData;
import top.cyblogs.data.DownloadList;
import top.cyblogs.data.SettingsData;
import top.cyblogs.model.DownloadItem;
import top.cyblogs.model.TempDownloadItem;
import top.cyblogs.model.enums.DownloadType;
import top.cyblogs.model.enums.ServiceType;
import top.cyblogs.util.FileUtils;
import top.cyblogs.util.StringUtils;
import top.cyblogs.utils.BiliBiliUtils;

import java.io.File;

public class AvDownloader {

    public static void download(String url) {
        String playId = BiliBiliUtils.getPlayId(url);
        // 获取视频初始化信息
        JsonNode initialState = AvApi.getInitialState(playId);
        // 获取分P
        JsonNode pages = initialState.findValue("pages");

        if (pages.size() == 0) {
            return;
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
            String downloadId = StringUtils.md5(targetFile.getAbsolutePath());

            if (dash != null) {
                String[] dashUrl = getDashUrl(dash);
                TempDownloadItem tempDownloadItem = new TempDownloadItem(downloadId, targetFile.getName(), ServiceType.SEPERATE, null, dashUrl, targetFile, BiliBiliData.header(), videoStatus);
                DownloadList.tempList.add(tempDownloadItem);
                return;
            }
            // 如果视频为durl
            JsonNode durl = videoUrl.findValue("durl");
            if (durl != null) {
                String[] durlUrl = getDurlUrl(durl);
                TempDownloadItem tempDownloadItem = new TempDownloadItem(downloadId, targetFile.getName(), ServiceType.SEGMENT, null, durlUrl, targetFile, BiliBiliData.header(), videoStatus);
                DownloadList.tempList.add(tempDownloadItem);
            }
        });
    }

    /**
     * 下载Dash视频
     */
    private static String[] getDashUrl(JsonNode dash) {
        String videoUrl = dash.findValue("video").get(0).findValue("baseUrl").asText();
        String audioUrl = dash.findValue("audio").get(0).findValue("baseUrl").asText();
        return new String[]{videoUrl, audioUrl};
    }

    /**
     * 下载Durl视频
     */
    private static String[] getDurlUrl(JsonNode durl) {
        String[] flvUrls = new String[durl.size()];
        for (int i = 0; i < durl.size(); i++) {
            flvUrls[i] = durl.get(i).findValue("url").asText();
        }
        return flvUrls;
    }
}
