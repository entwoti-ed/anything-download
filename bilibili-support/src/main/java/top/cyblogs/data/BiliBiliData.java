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
     * 当前的Cookie
     */
    public static String cookie = "";

    public static Map<String, String> header() {
        HashMap<String, String> header = new HashMap<>();
        header.put("Cookie", cookie);
        return header;
    }
}
