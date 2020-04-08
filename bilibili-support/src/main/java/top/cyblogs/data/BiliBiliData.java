package top.cyblogs.data;

import java.util.HashMap;
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
    public static String cookie;

    /**
     * 头信息
     *
     * @return header
     */
    public static Map<String, String> header() {
        HashMap<String, String> header = new HashMap<>();
        header.put("User-Agent", HttpData.USER_AGENT);
        header.put("Referer", HOME_URL);
        return header;
    }
}
