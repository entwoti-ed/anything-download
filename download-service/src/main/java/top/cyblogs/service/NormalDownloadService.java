package top.cyblogs.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import top.cyblogs.data.SettingsData;
import top.cyblogs.listener.DownloadListener;
import top.cyblogs.model.DownloadItem;
import top.cyblogs.model.enums.DownloadStatus;
import top.cyblogs.utils.DownloadUtils;
import top.cyblogs.utils.ServiceUtils;

import java.io.File;
import java.util.Map;

/**
 * 一般下载服务，主要用来下载无需事后处理的单文件，含大文件和小文件
 *
 * @author CY
 */
@Slf4j
public class NormalDownloadService {

    private Integer currentRetryCount = 0;

    public static void download(String url, File targetFile, Map<String, String> header, DownloadItem downloadStatus) {
        new NormalDownloadService().execDownload(url, targetFile, header, downloadStatus);
    }

    /**
     * 下载分段视频并合并
     *
     * @param targetFile 下载到目标位置
     * @param header     下载所需的请求头
     */
    private void execDownload(String url, File targetFile, Map<String, String> header, DownloadItem downloadStatus) {

        downloadStatus.setDownloadId(SecureUtil.md5(FileUtil.getCanonicalPath(targetFile)));
        String name = targetFile.getName();
        int lastIndexOf = name.lastIndexOf(".");
        downloadStatus.setFileName(lastIndexOf == -1 ? name : name.substring(0, lastIndexOf));
        downloadStatus.setTargetPath(FileUtil.getCanonicalPath(targetFile));
        downloadStatus.setStatus(DownloadStatus.WAITING);
        downloadStatus.setStatusFormat("等待下载...");
        downloadStatus.setProgressFormat("0%");
        downloadStatus.setProgress(0D);
        ServiceUtils.addToList(downloadStatus);

        if (SettingsData.skipIfExists && targetFile.exists()) {
            downloadStatus.setStatusFormat("文件已存在!");
            downloadStatus.setStatus(DownloadStatus.FINISHED);
            downloadStatus.setCurrentSpeed(null);
            downloadStatus.setTotalSize(FileUtil.readableFileSize(targetFile.length()));
            downloadStatus.setProgressFormat("100%");
            downloadStatus.setProgress(100D);
            return;
        }

        FileUtil.mkParentDirs(targetFile);

        DownloadUtils.download(url, targetFile, header, new DownloadListener() {

            @Override
            public void connecting(String url) {
                downloadStatus.setStatusFormat("正在连接...");
            }

            @Override
            public void start(long length) {
                // 开始下载
                downloadStatus.setStatusFormat("正在下载...");
                downloadStatus.setStatus(DownloadStatus.DOWNLOADING);
                downloadStatus.setTotalSize(FileUtil.readableFileSize(length));
            }

            @Override
            public void downloading(double progress, long speed, long time) {
                downloadStatus.setCurrentSpeed(FileUtil.readableFileSize(speed) + "/S");
                downloadStatus.setProgress(progress * 100);
                downloadStatus.setProgressFormat(ServiceUtils.ratioString(progress));
            }

            @Override
            public void downloadError(Exception e) {
                // 下面的常量5，当时放在data包下面，但是执行到这里就不执行了，有时间研究一下这是为什么？
                if (++currentRetryCount <= 5) {
                    log.info("正在重试下载...");
                    downloadStatus.setStatusFormat("重试下载...");
                    DownloadUtils.download(url, targetFile, header, this);
                }
            }

            @Override
            public void over(long time) {
                downloadStatus.setStatusFormat("下载完成!");
                downloadStatus.setStatus(DownloadStatus.FINISHED);
                downloadStatus.setCurrentSpeed(null);
                downloadStatus.setProgress(100D);
                downloadStatus.setProgressFormat("100%");
            }
        });

    }
}
