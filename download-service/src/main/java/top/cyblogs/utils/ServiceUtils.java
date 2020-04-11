package top.cyblogs.utils;

import lombok.extern.slf4j.Slf4j;
import top.cyblogs.data.DownloadList;
import top.cyblogs.exception.AlreadyExistsException;
import top.cyblogs.model.DownloadItem;

import java.text.DecimalFormat;

/**
 * 工具
 *
 * @author CY 测试通过
 */
@Slf4j
public class ServiceUtils {

    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("0.00");

    /**
     * 格式化进度信息
     */
    public static String ratioString(long current, long total, boolean isPercent) {
        if (total == 0) {
            return isPercent ? "--%" : "-/-";
        }
        return isPercent ? ratioString((double) current / total) : current + "/" + total;
    }

    /**
     * 格式化进度信息
     */
    public static String ratioString(double progress) {
        return PERCENT_FORMAT.format(progress * 100) + "%";
    }

    /**
     * 添加下载状态到列表中
     *
     * @param downloadStatus 下载状态
     */
    public static void addToList(DownloadItem downloadStatus) {
        if (DownloadList.list.stream().anyMatch(x -> x.getDownloadId().equals(downloadStatus.getDownloadId()))) {
            throw new AlreadyExistsException("重复添加了...");
        }
        DownloadList.list.add(downloadStatus);
    }
}
