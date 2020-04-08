package top.cyblogs.api;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import top.cyblogs.util.JacksonUtils;
import top.cyblogs.utils.BiliBiliUtils;

/**
 * AV的API
 *
 * @author CY
 * @apiNote BiliBili AV API UP主们
 * 2019年7月15日
 */
public class AvApi {

    /**
     * 获取视频链接API
     */
    private static final String VIDEO_URL = "https://api.bilibili.com/x/player/playurl?avid=%s&cid=%s&qn=1024&type=json&otype=json&fnver=0&fnval=16";

    /**
     * AV首页的URL用来获取初始信息
     */
    private static final String AV_INDEX_URL = "https://www.bilibili.com/video/av%s";

    /**
     * 获取视频的播放链接
     *
     * @param avid AV号
     * @param cid  弹幕号
     * @return 视频播放链接列表
     */
    public static JsonNode getVideoUrl(String avid, String cid) {
        String json = BiliBiliUtils.urlText(String.format(VIDEO_URL, avid, cid));
        return JacksonUtils.toJsonNode(json);
    }

    /**
     * 获取视频的详情信息
     *
     * @param avid AV号
     * @return 视频详情信息
     */
    public static JsonNode getInitialState(String avid) {
        String html = BiliBiliUtils.urlText(String.format(AV_INDEX_URL, avid));
        String json = StrUtil.subBetween(html, "<script>window.__INITIAL_STATE__=", "</script>");
        return JacksonUtils.toJsonNode(json);
    }
}
