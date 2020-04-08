package top.cyblogs.utils;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import top.cyblogs.data.BiliBiliData;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 下载器的公用方法类
 * 2019年7月15日
 */
public class BiliBiliUtils {

    private static Pattern PLAY_ID = Pattern.compile("\\d+");

    /**
     * 获取播放的ID OK
     *
     * @return 播放ID
     */
    public static String getPlayId(String url) {
        try {
            String path = new URL(url).getPath();
            Matcher matcher = PLAY_ID.matcher(path);
            if (matcher.find()) {
                return matcher.group(0);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("添加下载的URL格式不正确!");
        }
        return null;
    }

    /**
     * 获取URL中的文本
     */
    public static String urlText(String url) {
        HttpRequest request = HttpUtil.createGet(url);
        request.cookie(BiliBiliData.cookie);
        BiliBiliData.header().forEach(request::header);
        return request.execute().body();
    }
}
