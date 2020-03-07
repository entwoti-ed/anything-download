package top.cyblogs.support;

import com.fasterxml.jackson.databind.JsonNode;
import top.cyblogs.api.CommonApi;
import top.cyblogs.data.BiliBiliData;
import top.cyblogs.data.LoginSession;
import top.cyblogs.download.downloader.*;
import top.cyblogs.download.enums.DownloadType;
import top.cyblogs.exception.ForbiddenException;

/**
 * 下载列表服务，根据用户输入的URL获取下载列表
 *
 * @author CY
 */
public class BiliBiliSupport {

    public static void start(String url, String cookie) {

        setCookie(cookie);

        // 下载普通UP主的视频
        if (url.contains(DownloadType.AV.getUrlPrefix())) {
            AvDownloader.download(url);
        }
        // 下载BiliBili音频去的整张专辑
        else if (url.contains(DownloadType.AM.getUrlPrefix())) {
            AmDownloader.download(url);
        }
        // 下载BiliBili的音频
        else if (url.contains(DownloadType.AU.getUrlPrefix())) {
            AuDownloader.download(url);
        }
        // 下载BiliBili的电视剧，电影，纪录片等
        else if (url.contains(DownloadType.EP.getUrlPrefix())) {
            EpDownloader.download(url);
        }
        // 下载BiliBili的直播
        else if (url.contains(DownloadType.LIVE.getUrlPrefix())) {
            LiveDownloader.download(url);
        }
        // 下载BiliBili的番剧
        else if (url.contains(DownloadType.SS.getUrlPrefix())) {
            SsDownloader.download(url);
        }
        // 下载BiliBili的小视频
        else if (url.contains(DownloadType.VC.getUrlPrefix())) {
            VcDownloader.download(url);
        }
        // 不被支持
        else {
            throw new IllegalArgumentException("暂时还不支持你的URL!");
        }
    }

    /**
     * 设置Cookie
     *
     * @param cookie Cookie
     */
    private static void setCookie(String cookie) {
        // 读取硬盘上的Session
        String session = LoginSession.getSession().getBilibiliSession();

        if (session == null || !isSessionValid(session)) {
            // 硬盘上的Cookie不能用了，在检查前端的
            if (cookie != null && isSessionValid(cookie)) {
                LoginSession.getSession().setBilibiliSession(cookie).saveSession();
            } else {
                throw new ForbiddenException("需要Cookie!");
            }
        }
    }

    /**
     * Session是否有效
     */
    private static boolean isSessionValid(String cookie) {
        BiliBiliData.cookie = cookie;
        JsonNode userInfo = CommonApi.getUserInfo();
        return userInfo.findValue("isLogin").asBoolean();
    }
}
