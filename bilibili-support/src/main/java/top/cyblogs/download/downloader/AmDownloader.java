package top.cyblogs.download.downloader;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import top.cyblogs.api.AmApi;
import top.cyblogs.api.AuApi;
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

public class AmDownloader {

    public static String download(String url) {

        String listId = IdUtil.fastSimpleUUID();
        List<TempDownloadItem> list = new ArrayList<>();

        String playId = BiliBiliUtils.getPlayId(url);

        System.err.println(playId);

        JsonNode albumInfo = AmApi.getAlbumInfo(playId);
        String albumTitle = albumInfo.findValue("title").asText();
        JsonNode albumList = AmApi.getAlbumList(playId);

        albumList.findValue("data").findValue("data").forEach(x -> {
            String author = x.findValue("author").asText();
            String title = x.findValue("title").asText();
            String lyric = x.findValue("lyric").asText();


            String filePath = FileUtils.getPrettyFileName(albumTitle) + File.separator
                    + FileUtils.getPrettyFileName(author + (StrUtil.isNotBlank(author) ? " - " : "") + title);

            JsonNode musicUrl = AuApi.getMusicUrl(x.findValue("id").asText());
            String music = musicUrl.findValue("data").findValue("cdns").get(0).asText();

            DownloadItem mp3Status = new DownloadItem();
            mp3Status.setSource(BiliBiliData.SOURCE);
            mp3Status.setDownloadType(DownloadType.AUDIO);
            File targetFile = new File(SettingsData.path + filePath + ".mp3");

            list.add(
                    TempDownloadItem.init(music, targetFile, ServiceType.NORMAL, BiliBiliData.header(), mp3Status)
            );

            if (StrUtil.isNotBlank(lyric)) {
                DownloadItem lrcStatus = new DownloadItem();
                lrcStatus.setSource(BiliBiliData.SOURCE);
                lrcStatus.setDownloadType(DownloadType.LRC);
                File lrcTargetFile = new File(SettingsData.path + filePath + ".lrc");
                list.add(
                        TempDownloadItem.init(lyric, lrcTargetFile, ServiceType.NORMAL, BiliBiliData.header(), lrcStatus)
                );
            }

            long aid = x.findValue("aid").asLong();
            if (aid > 0) {
                AvDownloader.download("https://www.bilibili.com/video/av" + aid);
            }
        });

        DownloadList.tempMap.put(listId, list);
        return listId;
    }
}
