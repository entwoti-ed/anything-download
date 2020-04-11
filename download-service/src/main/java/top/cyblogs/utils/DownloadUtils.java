package top.cyblogs.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import top.cyblogs.exception.BadRequestException;
import top.cyblogs.listener.DownloadListener;

import java.io.File;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO 暂时不添加断点续传了, 因为要考虑的东西就太多了, 之前写过一个工具类, 代码并不好维护
 *
 * @author CY
 */
@Slf4j
public class DownloadUtils {

    private static final ExecutorService DOWNLOAD;

    private static Long speeds = 0L;

    static {
        ThreadFactory downloadFactory = ThreadFactoryBuilder.create().setNamePrefix("download-utils-").build();
        DOWNLOAD = new ThreadPoolExecutor(20, 2 << 12, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), downloadFactory);
    }

    public static void download(String url, File out, Map<String, String> header, DownloadListener listener) {

        DOWNLOAD.submit(() -> {
            try {
                executeDownload(url, out, header, listener);
            } catch (Exception e) {
                listener.downloadError(e);
            }
        });
    }

    private static void executeDownload(String url, File out, Map<String, String> header, DownloadListener listener) {

        // 计时开始
        log.debug("添加链接: {}", url);
        long start = System.currentTimeMillis();

        // 新建文件夹
        FileUtil.mkParentDirs(out);

        // 连接中...
        log.debug("正在连接...");
        listener.connecting(url);

        // 发起请求
        HttpRequest request = HttpRequest.get(url);
        header.forEach(request::header);
        final HttpResponse response = request.executeAsync();

        int status = response.getStatus();
        log.debug("响应状态码 ==> {}", status);
        if (status != HttpStatus.HTTP_OK) {
            log.debug("状态码异常, 停止下载...");
            throw new BadRequestException("请求失败: " + status);
        }

        // 文件大小
        long contentLength = Long.parseLong(response.header("Content-Length"));
        log.debug("文件大小 ==> {}", FileUtil.readableFileSize(contentLength));

        if (contentLength == out.length()) {
            log.debug("文件已经存在, 停止下载...");
            listener.over(System.currentTimeMillis() - start);
            return;
        }

        // 保存实时下载大小
        AtomicLong currentLength = new AtomicLong(0);
        AtomicLong lastLength = new AtomicLong(0);

        // 定时任务，用来实时计算下载速度
        ThreadFactory threadFactory = ThreadFactoryBuilder.create().setNamePrefix("download-speed-").build();
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1, threadFactory);

        // 执行定时任务
        executor.scheduleAtFixedRate(() -> {
            // 获取当前已经下载了多少
            long length = currentLength.get();
            if (length == 0) {
                return;
            }
            long currentSpeed = length - lastLength.get();
            speeds += currentSpeed;
            listener.downloading(contentLength <= 0 ? 0 : (double) length / contentLength, currentSpeed,
                    contentLength <= 0 || currentSpeed <= 0 ? -1 : (contentLength - length) / currentSpeed);
            lastLength.set(length);
        }, 0, 1, TimeUnit.SECONDS);

        response.writeBody(out, new StreamProgress() {

            @Override
            public void start() {
                log.debug("下载已开始...");
                listener.start(contentLength);
            }

            @Override
            public void progress(long progressSize) {
                currentLength.set(progressSize);
            }

            @Override
            public void finish() {
                executor.shutdownNow();
                listener.over(System.currentTimeMillis() - start);
                log.debug("下载完成...");
            }
        });
    }

    public static Long globalStatus() {
        long temp = speeds;
        speeds = 0L;
        return temp;
    }
}
