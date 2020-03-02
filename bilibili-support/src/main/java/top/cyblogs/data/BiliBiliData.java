package top.cyblogs.data;

import java.util.Map;

/**
 * BiliBili的数据
 */
public class BiliBiliData {
    /**
     * 来源
     */
    public static final String SOURCE = "哔哩哔哩";
    /**
     * BiliBili首页
     */
    public static final String HOME_URL = "https://www.bilibili.com/";
    /**
     * 当前的Cookie
     */
    public static String cookie = "";

    public static Map<String, String> header() {
        return Map.of("User-Agent", HttpData.USER_AGENT,
                "Origin", HOME_URL,
                "Referer", HOME_URL,
                "Cookie", cookie);
    }
}
