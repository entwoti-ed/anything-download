package top.cyblogs.download;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import top.cyblogs.Aria2c;
import top.cyblogs.data.PathData;
import top.cyblogs.input.Aria2cOptions;
import top.cyblogs.output.Aria2cGlobalStat;
import top.cyblogs.output.Aria2cStatus;
import top.cyblogs.start.Aria2cRpcOptions;
import top.cyblogs.support.DownloadTaskStatus;
import top.cyblogs.support.Options;
import top.cyblogs.support.Secret;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 下载工具，对接Aria2c
 *
 * @author CY
 */
@Slf4j
public class DownloadUtils {

    /**
     * 下载状态观察
     */
    private static StatusObserver observer = new StatusObserver();

    /**
     * Aria2c工具
     */
    private static Aria2c aria2c;

    /**
     * 连接Aria2c的密钥
     */
    private static String token;

    static {
        // Aria2的启动选项
        List<String> startOptions = new ArrayList<>();
        // 默认的下载目录
        startOptions.add("--dir=" + PathData.TEMP_FILE_PATH);
        // 检查文件的完整性
        startOptions.add("--check-integrity=true");
        // continue=true
        startOptions.add("--continue=true");
        // 单个任务最大线程数
        startOptions.add("--split=20");
        // 最大同时下载任务数
        startOptions.add("--max-concurrent-downloads=20");
        // 启动
        aria2c = Aria2c.run(startOptions);
        // 密钥
        token = Secret.token(Aria2cRpcOptions.getInstance().getRpcSecret());

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNamePrefix("download-listener-pool-").build();
        ScheduledExecutorService pool = new ScheduledThreadPoolExecutor(1, namedThreadFactory);

        // 做任务监听，每秒钟发送一次请求
        pool.scheduleAtFixedRate(() -> {
            ArrayList<Aria2cStatus> status = new ArrayList<>();
            status.addAll(Arrays.asList(aria2c.tellActive(token, new String[]{})));
            status.addAll(Arrays.asList(aria2c.tellWaiting(token, 0, Integer.MAX_VALUE, new String[]{})));
            status.addAll(Arrays.asList(aria2c.tellStopped(token, 0, Integer.MAX_VALUE, new String[]{})));
            observer.setStatusList(status);
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 下载一个链接
     *
     * @param header           请求头
     * @param downloadListener 监听器
     */
    public static void download(String url, File out, Map<String, String> header, BaseDownloadListener downloadListener) {

        Aria2cOptions options = Aria2cOptions.builder()
                .header(getHeader(header))
                // 输出目录
                .dir(FileUtil.getCanonicalPath(out.getParentFile()))
                // 输出文件
                .out(out.getName())
                .build();

        // 开始下载
        String gid = aria2c.addUri(token, new String[]{url},
                Options.of(options), Integer.MAX_VALUE);

        // 下载回调
        observer.getPropertyChangeSupport().addPropertyChangeListener(event -> {
            // 防止吃异常
            try {
                callBack(gid, downloadListener, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static void callBack(String gid, BaseDownloadListener downloadListener, PropertyChangeEvent event) {
        List<Aria2cStatus> statuses = (List<Aria2cStatus>) event.getNewValue();
        Aria2cStatus status = statuses.stream().filter(x -> gid.equals(x.getGid())).findFirst().orElse(null);
        if (downloadListener != null && status != null) {
            switch (status.getStatus()) {
                case DownloadTaskStatus.ACTIVE: {
                    downloadListener.active(status);
                    break;
                }
                case DownloadTaskStatus.COMPLETE: {
                    downloadListener.complete(status);
                    break;
                }
                case DownloadTaskStatus.ERROR: {
                    downloadListener.error(status);
                    break;
                }
                case DownloadTaskStatus.PAUSED: {
                    downloadListener.paused(status);
                    break;
                }
                case DownloadTaskStatus.REMOVED: {
                    downloadListener.removed(status);
                    break;
                }
                case DownloadTaskStatus.USED: {
                    downloadListener.used(status);
                    break;
                }
                case DownloadTaskStatus.WAITING: {
                    downloadListener.waiting(status);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    public static Aria2cGlobalStat globalStatus() {
        return aria2c.getGlobalStat(token);
    }

    /**
     * 获取Aria2适用的Header
     */
    private static String[] getHeader(Map<String, String> header) {
        if (header == null) {
            return new String[0];
        }
        List<String> headers = new ArrayList<>();
        header.forEach((key, value) -> headers.add(String.format("%s: %s", key, value)));
        return headers.toArray(new String[]{});
    }
}
