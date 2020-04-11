package top.cyblogs.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.crypto.SecureUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import top.cyblogs.data.PathData;
import top.cyblogs.data.SettingsData;
import top.cyblogs.listener.DownloadListener;
import top.cyblogs.listener.MergeVideoListener;
import top.cyblogs.model.DownloadItem;
import top.cyblogs.model.enums.DownloadStatus;
import top.cyblogs.utils.DownloadUtils;
import top.cyblogs.utils.ServiceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 下载临时文件服务, 仅在当前模块使用
 * <p>
 *
 * @author CY
 */
@Slf4j
class TempDownloadService {

    /**
     * 下载视频集合并合并
     *
     * @param urls       视频集合URL
     * @param targetFile 下载到目标位置
     * @param header     下载所需的请求头
     */
    void download(String[] urls, File targetFile, Map<String, String> header,
                  DownloadItem downloadStatus, MergeVideoListener listener) {

        String name = targetFile.getName();
        int lastIndexOf = name.lastIndexOf(".");
        downloadStatus.setFileName(lastIndexOf == -1 ? name : name.substring(0, lastIndexOf));
        downloadStatus.setTargetPath(FileUtil.getCanonicalPath(targetFile));
        downloadStatus.setStatus(DownloadStatus.WAITING);
        downloadStatus.setStatusFormat("等待下载...");
        downloadStatus.setProgressFormat("0%");
        downloadStatus.setProgress(0D);
        downloadStatus.setDownloadId(SecureUtil.md5(FileUtil.getCanonicalPath(targetFile)));
        ServiceUtils.addToList(downloadStatus);

        // 跳过已经存在
        if (SettingsData.skipIfExists && targetFile.exists()) {
            downloadStatus.setStatusFormat("文件已存在!");
            downloadStatus.setStatus(DownloadStatus.FINISHED);
            downloadStatus.setCurrentSpeed(null);
            downloadStatus.setTotalSize(FileUtil.readableFileSize(targetFile.length()));
            downloadStatus.setProgressFormat("100%");
            downloadStatus.setProgress(100.0);
            return;
        }

        FileUtil.mkParentDirs(targetFile);

        // 临时文件列表
        List<File> tempFiles = new ArrayList<>();

        // 下载完成数量计数
        List<Downloading> listStatus = new ArrayList<>();

        for (int i = 0; i < urls.length; i++) {

            File tempFile = new File(PathData.TEMP_FILE_PATH + SecureUtil.md5(urls[i].split("\\?")[0]) + ".m4s");

            tempFiles.add(tempFile);

            // 初始化一个状态
            listStatus.add(new Downloading());

            int index = i;

            DownloadUtils.download(urls[index], tempFile, header, new DownloadListener() {

                @Override
                public void connecting(String url) {
                    downloadStatus.setStatusFormat("正在连接...");
                }

                @Override
                public void start(long length) {
                    downloadStatus.setStatusFormat("正在下载...");
                    downloadStatus.setStatus(DownloadStatus.DOWNLOADING);
                    listStatus.get(index).setLength(length);
                    length = listStatus.stream().map(Downloading::getLength).reduce(Long::sum).orElse(0L);
                    downloadStatus.setTotalSize(FileUtil.readableFileSize(length));
                }

                @Override
                public void downloading(double progress, long speed, long time) {
                    listStatus.get(index).setProcess(progress).setSpeed(speed).setTime(time);
                    double currentProgress = listStatus.stream().mapToDouble(Downloading::getProcess).average().orElse(0D);
                    long downloadSpeed = listStatus.stream().mapToLong(Downloading::getSpeed).reduce(Long::sum).orElse(0L);
                    downloadStatus.setProgress(currentProgress * 100);
                    downloadStatus.setProgressFormat(ServiceUtils.ratioString(progress));
                    downloadStatus.setCurrentSpeed(FileUtil.readableFileSize(downloadSpeed) + "/S");
                }

                @Override
                public void over(long time) {
                    listStatus.get(index).setFinish(true);
                    if (listStatus.stream().allMatch(Downloading::isFinish)) {
                        // 下载完成
                        downloadStatus.setCurrentSpeed(null);
                        downloadStatus.setProgressFormat("100%");
                        downloadStatus.setProgress(100.0);
                        downloadStatus.setStatusFormat("等待合并...");
                        if (listener != null) {
                            ThreadFactory downloadFactory = ThreadFactoryBuilder.create().setNamePrefix("download-utils-").build();
                            ExecutorService merge = new ThreadPoolExecutor(20, 2 << 12, 0L,
                                    TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), downloadFactory);
                            merge.submit(() -> {
                                listener.startMerge(tempFiles, targetFile, downloadStatus);
                                merge.shutdownNow();
                            });
                        }
                    }
                }

                @Override
                public void downloadError(Exception e) {
                    if (++currentRetryCount <= 5) {
                        log.info("正在重试下载...");
                        downloadStatus.setStatusFormat("重试下载...");
                        DownloadUtils.download(urls[index], targetFile, header, this);
                    }
                }
            });
        }
    }

    private Integer currentRetryCount = 0;

    @Data
    @Accessors(chain = true)
    private static class Downloading {
        private double process;
        private long speed;
        private long time;
        private long length;
        private boolean finish;
    }
}
