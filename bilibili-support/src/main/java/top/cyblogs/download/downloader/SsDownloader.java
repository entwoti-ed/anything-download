package top.cyblogs.download.downloader;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import top.cyblogs.api.EpApi;
import top.cyblogs.api.SsApi;
import top.cyblogs.data.BiliBiliData;
import top.cyblogs.data.DownloadList;
import top.cyblogs.data.SettingsData;
import top.cyblogs.model.DownloadItem;
import top.cyblogs.model.TempDownloadItem;
import top.cyblogs.model.enums.DownloadType;
import top.cyblogs.model.enums.ServiceType;
import top.cyblogs.util.FileUtils;
import top.cyblogs.utils.BiliBiliUtils;

import java.io.File;

public class SsDownloader {

    public static void main(String[] args) {
        download("https://www.bilibili.com/bangumi/play/ss29334/");
    }

    public static void download(String url) {
        String playId = BiliBiliUtils.getPlayId(url);
        JsonNode ssInitialState = SsApi.getSsInitialState(playId);

        // 标题
        String title = FileUtils.getPrettyFileName(ssInitialState.findValue("mediaInfo").findValue("title").asText());
        // 正片
        ssInitialState.findValue("epList").forEach(y -> downloadEp(y, title));

        ssInitialState.findValue("sections").forEach(x -> {
            String sectionTitle = FileUtils.getPrettyFileName(x.findValue("title").asText());
            x.findValue("epList").forEach(y -> downloadEp(y,
                    title + File.separator + sectionTitle));
        });
    }

    private static void downloadEp(JsonNode epItem, String parentPath) {

        String title = epItem.findValue("titleFormat").asText() + " " + epItem.findValue("longTitle").asText();

        String badge = epItem.get("badge").asText();
        if (StrUtil.isNotBlank(badge)) {
            title = "[" + badge + "] " + title;
        }

        String epTitle = parentPath + File.separator + FileUtils.getPrettyFileName(title);
        System.out.println(BiliBiliData.header());

        JsonNode videoUrl = EpApi.getVideoUrl(epItem.get("aid").asText(), epItem.get("cid").asText(), epItem.get("id").asText());
        System.out.println(videoUrl);

        DownloadItem videoStatus = new DownloadItem();
        videoStatus.setSource(BiliBiliData.SOURCE);
        videoStatus.setDownloadType(DownloadType.VIDEO);
        File targetFile = new File(SettingsData.path + epTitle + ".mp4");

        // 如果视频为dash
        JsonNode dash = videoUrl.findValue("dash");
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
