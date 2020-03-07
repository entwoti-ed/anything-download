package top.cyblogs.download.downloader;

import com.fasterxml.jackson.databind.JsonNode;
import top.cyblogs.api.VcApi;
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

public class VcDownloader {

    public static void download(String url) {
        String playId = BiliBiliUtils.getPlayId(url);
        System.out.println(playId);
        JsonNode playDetail = VcApi.getPlayDetail(playId);
        JsonNode playItem = playDetail.findValue("data").findValue("item");
        String title = FileUtils.getPrettyFileName(playItem.findValue("description").asText());
        String videoPlayUrl = playItem.findValue("video_playurl").asText();

        DownloadItem videoStatus = new DownloadItem();
        videoStatus.setSource(BiliBiliData.SOURCE);
        videoStatus.setDownloadType(DownloadType.VIDEO);
        File targetFile = new File(SettingsData.path + title + ".mp4");
        DownloadList.tempList.add(
                TempDownloadItem.init(videoPlayUrl, targetFile, ServiceType.SEGMENT, BiliBiliData.header(), videoStatus)
        );
    }
}
