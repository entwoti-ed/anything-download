package top.cyblogs.model.enums;

/**
 * 下载状态
 *
 * @author CY
 */
public enum DownloadStatus {

    /**
     * 等待中
     */
    WAITING,

    /**
     * 下载中
     */
    DOWNLOADING,

    /**
     * 合并
     */
    MERGING,

    /**
     * 下载完成
     */
    FINISHED
}