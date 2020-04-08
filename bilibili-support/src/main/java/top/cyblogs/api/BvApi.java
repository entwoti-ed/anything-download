package top.cyblogs.api;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import top.cyblogs.util.JacksonUtils;
import top.cyblogs.utils.BiliBiliUtils;

/**
 * BV
 */
@Slf4j
public class BvApi {

    private final String html;

    public BvApi(String path) {
        String url = "https://www.bilibili.com" + path;
        html = BiliBiliUtils.urlText(url);
    }

    /**
     * 获取视频的播放链接
     *
     * @return 视频播放链接列表
     */
    public JsonNode getVideoUrl() {
        String json = StrUtil.subBetween(html, "<script>window.__playinfo__=", "</script>");
        return JacksonUtils.toJsonNode(json);
    }

    /**
     * 获取视频的详情信息
     *
     * @return 视频详情信息
     */
    public JsonNode getInitialState() {
        String json = StrUtil.subBetween(html, "<script>window.__INITIAL_STATE__=", "</script>");
        return JacksonUtils.toJsonNode(json);
    }
}
