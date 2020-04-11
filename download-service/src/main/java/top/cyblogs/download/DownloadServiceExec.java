package top.cyblogs.download;

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
        if (list.stream().allMatch(x -> x.getTargetFile().exists())) {
            throw new AlreadyExistsException("文件都已经在硬盘上了, 如果某个文件损坏, 请删除文件后再次启动程序...");
        }
        list.forEach(x -> {
            switch (x.getServiceType()) {
                case HLS: {
                    HlsVideoService.download(x.getUrl(), x.getTargetFile(), x.getDownloadStatus());
                    break;
                }
                case NORMAL: {
                    NormalDownloadService.download(x.getUrl(), x.getTargetFile(), x.getHeader(), x.getDownloadStatus());
                    break;
                }
                case SEGMENT: {
                    SegmentVideoService.download(x.getUrls(), x.getTargetFile(), x.getHeader(), x.getDownloadStatus());
                    break;
                }
                case SEPERATE: {
                    SeperateVideoService.download(x.getUrls(), x.getTargetFile(), x.getHeader(), x.getDownloadStatus());
                    break;
                }
                default: {
                    // ignored
                }
            }
        });
    }
}
