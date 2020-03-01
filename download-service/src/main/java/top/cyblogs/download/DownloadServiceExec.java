package top.cyblogs.download;

import top.cyblogs.data.SettingsData;
import top.cyblogs.exception.AlreadyExistsException;
import top.cyblogs.model.TempDownloadItem;
import top.cyblogs.service.HlsVideoService;
import top.cyblogs.service.NormalDownloadService;
import top.cyblogs.service.SegmentVideoService;
import top.cyblogs.service.SeperateVideoService;

import java.util.List;

/**
 * 执行下载服务
 *
 * @author CY
 */
public class DownloadServiceExec {

    public static void startDownload(List<TempDownloadItem> list) {
        if (SettingsData.skipIfExists && list.stream().allMatch(x -> x.getTargetFile().exists())) {
            throw new AlreadyExistsException("文件都已经在硬盘上了...");
        }
        list.forEach(x -> {
            switch (x.getServiceType()) {
                case HLS: {
                    HlsVideoService.download(x.getUrl(), x.getTargetFile(), x.getInitialStatus());
                    break;
                }
                case NORMAL: {
                    NormalDownloadService.download(x.getUrl(), x.getTargetFile(), x.getHeader(), x.getInitialStatus());
                    break;
                }
                case SEGMENT: {
                    SegmentVideoService.download(x.getUrls(), x.getTargetFile(), x.getHeader(), x.getInitialStatus());
                    break;
                }
                case SEPERATE: {
                    SeperateVideoService.download(x.getUrls(), x.getTargetFile(), x.getHeader(), x.getInitialStatus());
                    break;
                }
                default: {
                    // ignored
                }
            }
        });
    }
}
