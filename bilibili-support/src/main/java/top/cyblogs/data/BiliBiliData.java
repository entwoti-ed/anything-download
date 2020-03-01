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
     * TODO 当前的Cookie
     */
    public static String cookie = "CURRENT_FNVAL=16; _uuid=DF3FB83A-34D4-644C-427B-3017310B9DE767461infoc; buvid3=697E9950-3BD8-4553-BE72-D83CF957B4D3155813infoc; stardustvideo=1; rpdid=|(u|kukJ~kY)0J'ulYYkmmm~Y; LIVE_BUVID=AUTO8315663040781962; laboratory=1-1; im_notify_type_10521774=0; sid=imsqcv5w; _ga=GA1.2.440584044.1572084267; DedeUserID=10521774; DedeUserID__ckMd5=1171e30f3258fb2c; SESSDATA=5445f8c9%2C1583030172%2C1ac20711; bili_jct=d7f5dc9dd786efdd14e34745b809e026; CURRENT_QUALITY=80; INTVER=1; bp_t_offset_10521774=361459023090083334; stardustpgcv=0606";
    /**
     * 每次请求的访问头
     */
    public static Map<String, String> header = Map.of(
            "Origin", HOME_URL,
            "User-Agent", HttpData.USER_AGENT,
            "Referer", HOME_URL,
            "Cookie", cookie);
}
