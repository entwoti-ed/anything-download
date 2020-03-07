package top.cyblogs.service;

import org.springframework.stereotype.Service;
import top.cyblogs.data.DownloadList;
import top.cyblogs.data.SettingsData;
import top.cyblogs.download.DownloadServiceExec;
import top.cyblogs.exception.AlreadyExistsException;
import top.cyblogs.exception.NothingException;
import top.cyblogs.exception.UrlNotSupportException;
import top.cyblogs.model.DownloadItem;
import top.cyblogs.model.TempDownloadItem;
import top.cyblogs.model.enums.SupportUrl;
import top.cyblogs.model.params.AddDownload;
import top.cyblogs.model.params.NormalDownload;
import top.cyblogs.support.BiliBiliSupport;
import top.cyblogs.util.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 应用总服务
 *
 * @author CY
 */
@Service
public class ApplicationService {

    public List<TempDownloadItem> download(AddDownload download) {
        DownloadList.tempList.clear();
        String url = download.getUrl();
        if (url.contains(SupportUrl.BILIBILI.getUrlContains())) {
            BiliBiliSupport.start(url, download.getCookie());
        } else {
            throw new UrlNotSupportException("您要下载的URL还没有被支持, 请查看支持列表");
        }
        if (DownloadList.tempList.size() == 0) {
            throw new NothingException("啥都没有获取到! 可能你没有购买VIP或者视频，如有其他原因请issues...");
        }
        return DownloadList.tempList;
    }

    public void startDownload(List<String> downloadList) {
        List<String> idList = downloadList.stream().filter(downloadId -> DownloadList.list.stream().noneMatch(downloadItem -> downloadItem.getDownloadId().equals(downloadId))).collect(Collectors.toList());
        if (idList.size() == 0) {
            throw new AlreadyExistsException("你要下载的内容本来就在列表中了!");
        }
        List<TempDownloadItem> downloadItems = DownloadList.tempList.stream().filter(x -> idList.contains(x.getDownloadId())).collect(Collectors.toList());
        DownloadServiceExec.startDownload(downloadItems);
    }

    public void normalDownload(NormalDownload normalDownload) {
        File targetFile = new File(SettingsData.path + FileUtils.getPrettyFileName(normalDownload.getTitle()));
        DownloadItem downloadStatus = new DownloadItem().setSource(normalDownload.getSource())
                .setDownloadType(normalDownload.getDownloadType());
        Map<String, String> header = new HashMap<>();
        header.put("Cookie", normalDownload.getCookie());
        switch (normalDownload.getServiceType()) {
            case HLS: {
                HlsVideoService.download(normalDownload.getUrl(), targetFile, downloadStatus);
                break;
            }
            case SEPERATE: {
                SeperateVideoService.download(normalDownload.getUrls(), targetFile, header, downloadStatus);
                break;
            }
            case SEGMENT: {
                SegmentVideoService.download(normalDownload.getUrls(), targetFile, header, downloadStatus);
                break;
            }
            case NORMAL: {
                NormalDownloadService.download(normalDownload.getUrl(), targetFile, header, downloadStatus);
                break;
            }
            default: {
                // ignore
            }
        }
    }
}
