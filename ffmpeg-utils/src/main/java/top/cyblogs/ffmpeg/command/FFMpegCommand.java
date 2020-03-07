package top.cyblogs.ffmpeg.command;

import cn.hutool.core.io.FileUtil;
import top.cyblogs.data.PathData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FFMpeg的命令
 *
 * @author CY
 */
public class FFMpegCommand {

    /**
     * FFMpeg的路径
     */
    private static final String FFMPEG = FileUtil.getCanonicalPath(PathData.FFMPEG);

    /**
     * 使用分离器文件合并视频的命令
     */
    public static List<String> mergeVideo(File seperator, File out) {
        ArrayList<String> command = new ArrayList<>();
        command.add(String.format("\"%s\"", FFMPEG));
        command.add("-f");
        command.add("concat");
        command.add("-safe");
        command.add("0");
        command.add("-i");
        command.add(String.format("\"%s\"", FileUtil.getCanonicalPath(seperator)));
        command.add("-c");
        command.add("copy");
        command.add("-y");
        command.add(String.format("\"%s\"", FileUtil.getCanonicalPath(out)));
        return command;
    }

    /**
     * 音频和视频文件合并在一起的命令
     *
     * @param video 视频文件
     * @param audio 音频文件
     * @param out   输出文件
     * @return 命令
     */
    public static List<String> mergeVideoAndAudio(File video, File audio, File out) {
        ArrayList<String> command = new ArrayList<>();
        command.add(String.format("\"%s\"", FFMPEG));
        command.add("-i");
        command.add(String.format("\"%s\"", FileUtil.getCanonicalPath(video)));
        command.add("-i");
        command.add(String.format("\"%s\"", FileUtil.getCanonicalPath(audio)));
        command.add("-c:v");
        command.add("copy");
        command.add("-c:a");
        command.add("aac");
        command.add("-strict");
        command.add("experimental");
        command.add("-map");
        command.add("0:v:0?");
        command.add("-map");
        command.add("1:a:0?");
        command.add(String.format("\"%s\"", FileUtil.getCanonicalPath(out)));
        return command;
    }

    /**
     * 下载M3U8文件到视频的命令
     *
     * @return 命令
     */
    public static List<String> downloadM3U8(String m3U8Url, File out) {
        ArrayList<String> command = new ArrayList<>();
        command.add(String.format("\"%s\"", FFMPEG));
        command.add("-allowed_extensions");
        command.add("ALL");
        command.add("-protocol_whitelist");
        command.add("\"file,http,https,rtp,udp,tcp,tls,crypto\"");
        command.add("-i");
        command.add(String.format("\"%s\"", m3U8Url));
        command.add("-c");
        command.add("copy");
        command.add(String.format("\"%s\"", FileUtil.getCanonicalPath(out)));
        return command;
    }

    /**
     * 合并视频文件的命令（该命令使用了concat的方式，所以只能合并TS文件）
     *
     * @return 命令
     */
    public static List<String> mergeTs(List<File> videos, File out) {

        String videosJoin = videos.stream()
                .map(FileUtil::getCanonicalPath)
                .collect(Collectors.joining("|"));

        ArrayList<String> command = new ArrayList<>();
        command.add(String.format("\"%s\"", FFMPEG));
        command.add("-i");
        command.add(String.format("\"concat:%s\"", videosJoin));
        command.add("-c");
        command.add("copy");
        command.add("-bsf:a");
        command.add("aac_adtstoasc");
        command.add(String.format("\"%s\"", FileUtil.getCanonicalPath(out)));
        return command;
    }

    /**
     * 将视频转为TS格式的命令
     *
     * @return 命令
     */
    public static List<String> convert2Ts(File video, File out) {

        // 全部替换成.ts后缀
        String path = FileUtil.getCanonicalPath(out);
        String lastPath = path.substring(0, path.lastIndexOf(".")) + ".ts";

        ArrayList<String> command = new ArrayList<>();
        command.add(String.format("\"%s\"", FFMPEG));
        command.add("-i");
        command.add(String.format("\"%s\"", FileUtil.getCanonicalPath(video)));
        command.add("-vcodec");
        command.add("copy");
        command.add("-acodec");
        command.add("copy");
        command.add("-vbsf");
        command.add("h264_mp4toannexb");
        command.add(String.format("\"%s\"", lastPath));
        return command;
    }
}