package top.cyblogs.download.downloader;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import top.cyblogs.api.EpApi;
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
import java.util.ArrayList;
import java.util.List;

public class EpDownloader {

    public static String download(String url) {

        String listId = IdUtil.fastSimpleUUID();
        List<TempDownloadItem> list = new ArrayList<>();


        String playId = BiliBiliUtils.getPlayId(url);
        JsonNode epInitialState = EpApi.getEpInitialState(playId);
        // 标题
        String title = FileUtils.getPrettyFileName(epInitialState.findValue("mediaInfo").findValue("title").asText());
        // 正片
        epInitialState.findValue("epList").forEach(y -> downloadEp(y, title, list));

        epInitialState.findValue("sections").forEach(x -> {
            String sectionTitle = FileUtils.getPrettyFileName(x.findValue("title").asText());
            x.findValue("epList").forEach(y -> downloadEp(y,
                    title + File.separator + sectionTitle, list));
        });

        DownloadList.tempMap.put(listId, list);
        return listId;
    }

    private static void downloadEp(JsonNode epItem, String parentPath, List<TempDownloadItem> list) {


        String title = epItem.findValue("titleFormat").asText() + " " + epItem.findValue("longTitle").asText();

        String badge = epItem.get("badge").asText();
        if (StrUtil.isNotBlank(badge)) {
            title = "[" + badge + "] " + title;
        }

        String epTitle = parentPath + File.separator + FileUtils.getPrettyFileName(title);

        JsonNode videoUrl = EpApi.getVideoUrl(epItem.get("aid").asText(), epItem.get("cid").asText(), epItem.get("id").asText());

        DownloadItem videoStatus = new DownloadItem();
        videoStatus.setSource(BiliBiliData.SOURCE);
        videoStatus.setDownloadType(DownloadType.VIDEO);
        File targetFile = new File(SettingsData.path + epTitle + ".mp4");

        // 如果视频为dash
        JsonNode dash = videoUrl.findValue("dash");
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
