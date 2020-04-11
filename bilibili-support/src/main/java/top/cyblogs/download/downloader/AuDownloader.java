package top.cyblogs.download.downloader;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import top.cyblogs.api.AuApi;
import top.cyblogs.data.BiliBiliData;
import top.cyblogs.data.DownloadList;
import top.cyblogs.data.SettingsData;
import top.cyblogs.model.DownloadItem;
import top.cyblogs.model.TempDownloadItem;
import top.cyblogs.model.enums.DownloadType;
import top.cyblogs.model.enums.ServiceType;
import top.cyblogs.utils.BiliBiliUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AuDownloader {

    public static String download(String url) {

        String listId = IdUtil.fastSimpleUUID();
        List<TempDownloadItem> list = new ArrayList<>();

        String playId = BiliBiliUtils.getPlayId(url);
        JsonNode musicDetail = AuApi.getMusicDetail(playId);
        JsonNode detailData = musicDetail.findValue("data");
        String title = detailData.findValue("title").asText();
        String author = detailData.findValue("author").asText();
        if (StrUtil.isNotBlank(author)) {
            title = author + " - " + title;
        }
        long aid = detailData.findValue("aid").asLong();
        if (aid > 0) {
            AvDownloader.download("https://www.bilibili.com/video/av" + aid);
        }
        JsonNode musicUrl = AuApi.getMusicUrl(playId);
        String downloadUrl = musicUrl.findValue("data").findValue("cdns").get(0).asText();

        DownloadItem mp3Status = new DownloadItem();
        mp3Status.setSource(BiliBiliData.SOURCE);
        mp3Status.setDownloadType(DownloadType.AUDIO);
        File targetFile = new File(SettingsData.path + title + ".mp3");

        list.add(
                TempDownloadItem.init(downloadUrl, targetFile, ServiceType.NORMAL, BiliBiliData.header(), mp3Status)
        );

        DownloadList.tempMap.put(listId, list);
        return listId;
    }
}
