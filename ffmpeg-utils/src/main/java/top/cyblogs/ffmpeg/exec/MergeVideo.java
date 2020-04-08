package top.cyblogs.ffmpeg.exec;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import top.cyblogs.data.PathData;
import top.cyblogs.ffmpeg.command.FFMpegCommand;
import top.cyblogs.ffmpeg.exception.FFMpegException;
import top.cyblogs.ffmpeg.listener.FFMpegListener;
import top.cyblogs.ffmpeg.utils.ExecUtils;
import top.cyblogs.ffmpeg.utils.ProgressUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 合并视频
 *
 * @author CY 测试通过
 */
public class MergeVideo {

    /**
     * 合并视频文件
     *
     * @param videos   视频文件列表
     * @param out      文件输出的位置
     * @param listener 监听器
     */
    public synchronized static void exec(List<File> videos, File out, FFMpegListener listener) {

        // 检查参数
        if (videos == null || out == null) {
            throw new IllegalArgumentException("合并分段文件时参数异常!");
        }

        if (listener != null) {
            listener.start();
        }

        // 建立目标文件夹
        FileUtil.mkParentDirs(out);

        // 存在就删除
        FileUtil.del(out);

        // 将文件列表写入分离器文件
        File seperatorFile = new File(PathData.TEMP_FILE_PATH + IdUtil.fastSimpleUUID() + ".txt");
        FileUtil.mkParentDirs(seperatorFile);
        try (PrintWriter writer = new PrintWriter(seperatorFile, "UTF-8")) {
            for (File video : videos) {
                writer.println(String.format("file '%s'", FileUtil.getCanonicalPath(video).replace("'", "\\'")));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FFMpegException("视频合并时, 创建分离器文件出错...");
        }

        // 获取命令
        List<String> command = FFMpegCommand.mergeVideo(seperatorFile, out);

        ProgressUtils progressUtils = new ProgressUtils();
        // 执行命令
        ExecUtils.exec(command, s -> progressUtils.watchTimeProgress(s, listener));

        try {
            // 给一个延时，不要和FFMpeg进程抢资源
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 删除临时文件
        FileUtil.del(seperatorFile);

        if (listener != null) {
            listener.over();
        }
    }
}
