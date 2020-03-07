package top.cyblogs.init;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import top.cyblogs.data.PathData;

/**
 * Windows版FFMpeg下载
 */
@Slf4j
public class DownloadFFMpeg {

    /**
     * FFMpeg的下载链接
     */
    public static final String FFMPEG_URL = "https://ffmpeg.zeranoe.com/builds/win64/static/ffmpeg-20200306-cfd9a65-win64-static.zip";

    /**
     * 下载FFMpeg程序
     */
    public static void download() {

        if (PathData.FFMPEG.exists()) {
            return;
        }

        FileUtil.mkParentDirs(PathData.FFMPEG);

        final HttpResponse response = HttpRequest.get(FFMPEG_URL).executeAsync();
        long contentLength = Long.parseLong(response.header("Content-Length"));

        log.info("正在下载FFMpeg, 请稍等, 如果长时间无响应, 请手动重启程序...");
        log.info("您可以选择手动将FFMpeg拷贝到: " + FileUtil.getCanonicalPath(PathData.FFMPEG));

        ProgressBar pb = new ProgressBar("FFMpeg ==> ", contentLength, ProgressBarStyle.ASCII);

        response.writeBody(PathData.FFMPEG_ZIP, new StreamProgress() {

            @Override
            public void start() {
                pb.setExtraMessage("下载中...");
            }

            @Override
            public void progress(long progressSize) {
                pb.stepTo(progressSize);
            }

            @Override
            public void finish() {
                pb.setExtraMessage("下载完成...");
                pb.close();
            }
        });

        log.info("正在解压FFMpeg...");
        UnZipUtils.unZip(PathData.FFMPEG_ZIP, PathData.FFMPEG);

        log.info("删除FFMpeg临时文件...");
        FileUtil.del(PathData.FFMPEG_ZIP);

        response.close();
    }
}
