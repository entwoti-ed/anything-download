package com.cy.htmlparser;

import cn.hutool.core.util.StrUtil;
import com.cy.data.ImoocBaseData;
import com.cy.model.DownloadItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import top.cyblogs.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 初始化下载列表
 * 该类用于解析用户输入的Imooc目录链接，支持三种链接的解析
 * <p>
 * 免费的视频解析、试看的视频解析、收费的视频解析
 * <p>
 * 目前支持的类型：video、code、ceping、article、download更多类型待发现
 * <p>
 * 如果要增加“解析链接”或者增加“解析类型”，修改该类即可
 * 2019年7月10日
 *
 * @author CY
 */
public class DownloadListParser {

    private static final String COOKIE_NAME = "apsid";

    public static List<DownloadItem> parse(String url) throws IOException {

        if (url.contains(ImoocBaseData.FREE)) {
            // 免费视频
            return freeVideo(url);
        } else if (url.contains(ImoocBaseData.TRY)) {
            // 试看视频
            return tryVideo(url);
        } else if (url.contains(ImoocBaseData.PAY)) {
            // 收费视频
            return payVideo(url);
        } else {
            // Todo 其他链接
            return new ArrayList<>();
        }
    }

    /**
     * 下载免费的视频 Todo TEST LIST DATA OK
     * 当前可用 2019年7月4日
     * 包含：video,code,ceping,download
     *
     * @param url 链接
     */
    private static ArrayList<DownloadItem> freeVideo(String url) throws IOException {
        Document document = Jsoup.connect(url).cookie(COOKIE_NAME, ImoocBaseData.apsid).get();
        // 获取课程的名字 例如：微服务架构在二手交易平台（转转）中的实践
        final String title = document.select("h2.l").text().trim();
        ArrayList<DownloadItem> downloadList = new ArrayList<>();
        for (Element chapter : document.select(".chapter")) {
            // 章节标题 例如：第1章 微服务架构在二手交易平台（转转）中的实践
            String chapterTitle = chapter.select("h3").text();
            // 获取该章节下的每一个课程
            for (Element a : chapter.select("ul li a")) {
                String suffixUrl = a.attr("href");
                // 获取章节下面课程的名字
                String courseName = a.text();
                DownloadItem downloadItem = new DownloadItem();
                if (suffixUrl.contains("video")) {
                    String _id = StrUtil.subBetween(
                            Jsoup.connect(ImoocBaseData.HOME_URL + suffixUrl)
                                    .cookie(COOKIE_NAME, ImoocBaseData.apsid).get().toString(),
                            "OP_CONFIG.mongo_id=\"", "\";");
                    downloadItem.setUrl("https://www.imooc.com/course/playlist" + suffixUrl + "&_id=" + _id);
                    int i = courseName.lastIndexOf("(") == -1 ? courseName.length() : courseName.lastIndexOf("(") - 1;
                    courseName = FileUtils.getPrettyFileName(courseName.substring(0, i));
                    downloadItem.setFileName(courseName);
                    downloadItem.setOutputFile(new File(ImoocBaseData.path + title +
                            File.separator + chapterTitle + File.separator +
                            courseName + ImoocBaseData.videoFormat));
                    downloadItem.setType("video");
                } else if (suffixUrl.contains("code")) {
                    if (!ImoocBaseData.isDownloadText) {
                        continue;
                    }
                    downloadItem.setUrl(ImoocBaseData.HOME_URL + suffixUrl);
                    courseName = FileUtils.getPrettyFileName(courseName);
                    downloadItem.setFileName(courseName);
                    downloadItem.setOutputFile(new File(ImoocBaseData.path + title +
                            File.separator + chapterTitle + File.separator + courseName + ".html"));
                    downloadItem.setType("code");
                } else if (suffixUrl.contains("ceping")) {
                    if (!ImoocBaseData.isDownloadText) {
                        continue;
                    }
                    downloadItem.setUrl(ImoocBaseData.HOME_URL + suffixUrl);
                    courseName = FileUtils.getPrettyFileName(courseName);
                    downloadItem.setFileName(courseName);
                    downloadItem.setOutputFile(new File(ImoocBaseData.path + title +
                            File.separator + chapterTitle + File.separator + courseName + ".html"));
                    downloadItem.setType("ceping");
                } else {
                    // Todo
                }
                downloadList.add(downloadItem);
            }
        }
        // 以下内容需要登陆，所以给了一个默认账号进行登陆
        Elements as = document.select(".downlist a");
        for (Element a : as) {
            DownloadItem downloadItem = new DownloadItem();
            String downloadUrl = new URL(new URL(ImoocBaseData.HOME_URL), a.attr("href")).toString();
            downloadItem.setUrl(downloadUrl);
            String courseName = a.select("span:first-child").text();
            courseName = FileUtils.getPrettyFileName(courseName);
            downloadItem.setFileName(courseName);
            // 获取资料的后缀名
            String fileName = new File(new URL(downloadUrl).getPath()).getName();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            downloadItem.setOutputFile(new File(ImoocBaseData.path + title + File.separator + "资料" +
                    File.separator + courseName + extension));
            downloadItem.setType("download");
            downloadList.add(downloadItem);
        }
        return downloadList;
    }

    /**
     * 下载试看视频 Todo TEST LIST DATA OK
     * 当前可用 2020年2月5日
     * 包含：video
     *
     * @param url 试看的URL
     * @throws IOException #
     */
    private static ArrayList<DownloadItem> tryVideo(String url) throws IOException {
        Document document = Jsoup.connect(url).cookie(COOKIE_NAME, ImoocBaseData.apsid).get();
        final String title = document.select("h1.l").text(); // 下载的课程的名字
        String cid = document.select("[data-cid]").attr("data-cid"); // 获取课程的Id
        ArrayList<DownloadItem> downloadList = new ArrayList<>();
        for (Element chapter : document.select("li.chapter")) { // 获取章节
            String chapterTitle = chapter.select("h5").text(); // 章节的标题
            for (Element li : chapter.select("ul li")) { // 获取所有的章节课程
                // 播放的ID
                String playId = li.select("input[type=hidden]").val();
                if ("".equals(playId.trim())) {
                    continue; // 收费课程跳过
                }
                DownloadItem downloadItem = new DownloadItem();
                downloadItem.setUrl("https://coding.imooc.com/lesson/m3u8h5?mid=" + playId + "&cid=" + cid);
                downloadItem.setType("video");
                String courseName = li.select(".title_info").text();
                courseName = FileUtils.getPrettyFileName(courseName);
                downloadItem.setFileName(courseName);
                downloadItem.setOutputFile(new File(ImoocBaseData.path + title + File.separator + chapterTitle +
                        File.separator + courseName + ImoocBaseData.videoFormat));
                downloadList.add(downloadItem);
            }
        }
        return downloadList;
    }

    /**
     * 下载付费的视频 Todo TEST LIST DATA OK
     * 当前可用 2019年7月4日
     * 包含：video, article
     * <p>
     * 无法获取当前用户并没有购买的视频
     *
     * @param url 链接
     */
    private static ArrayList<DownloadItem> payVideo(String url) throws IOException {
        Document document = Jsoup.connect(url).cookie(COOKIE_NAME, ImoocBaseData.apsid).get();
        String title = document.select("h2.course-title").text(); // 下载的课程的名字
        String urlPathName = new File(new URL(url).getPath()).getName();
        String cid = urlPathName.substring(0, urlPathName.indexOf("."));
        ArrayList<DownloadItem> downloadList = new ArrayList<>();
        for (Element chapter : document.select(".list-item")) { // 获取章节
            String chapterTitle = chapter.select("h3").text(); // 章节标题
            for (Element li : chapter.select("ul li")) { // 获取章节下所有的章节课程
                // 获取章节下面课程的名字
                Elements icon = li.select("i");
                Elements a = icon.next();
                String courseName = a.select(".title_info").text().trim();
                String suffixUrl = a.attr("href"); // 取得URL的后缀
                String iconType = icon.attr("class"); // 得到图标的类型
                DownloadItem downloadItem = new DownloadItem();
                String mid = suffixUrl.substring(suffixUrl.indexOf("mid=") + "mid=".length());
                if ("imv2-video ileft".equalsIgnoreCase(iconType)) { // 视频
                    // 拼接清晰度选择地址
                    downloadItem.setUrl("https://coding.imooc.com/lesson/m3u8h5?mid=" + mid + "&cid=" + cid);
                    int i = courseName.lastIndexOf("(") == -1 ? courseName.length() : courseName.lastIndexOf("(") - 1;
                    courseName = FileUtils.getPrettyFileName(courseName.substring(0, i));
                    downloadItem.setFileName(courseName);
                    // 拼接出保存的地址
                    downloadItem.setOutputFile(new File(ImoocBaseData.path + title +
                            File.separator + chapterTitle + File.separator + courseName + ImoocBaseData.videoFormat));
                    downloadItem.setType("video");
                } else if ("imv2-article ileft".equalsIgnoreCase(iconType)) { // 文章
                    if (!ImoocBaseData.isDownloadText) {
                        continue;
                    }
                    downloadItem.setUrl("https://coding.imooc.com/lesson/ajaxmediainfo?mid=" + mid + "&cid=" + cid);
                    courseName = FileUtils.getPrettyFileName(courseName);
                    downloadItem.setFileName(courseName);
                    downloadItem.setOutputFile(new File(ImoocBaseData.path + title +
                            File.separator + chapterTitle + File.separator + courseName + ".html"));
                    downloadItem.setType("article");
                } else {
                    if (!ImoocBaseData.isDownloadText) {
                        continue;
                    }
                    // Todo
                }
                downloadList.add(downloadItem);
            }
        }
        return downloadList;
    }
}