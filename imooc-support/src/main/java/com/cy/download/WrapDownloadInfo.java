//package com.cy.download;
//
//import application.data.ApplicationInfo;
//import application.usage.info.UsageRecord;
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.io.IoUtil;
//import cn.hutool.crypto.Mode;
//import cn.hutool.crypto.Padding;
//import cn.hutool.crypto.SecureUtil;
//import cn.hutool.crypto.symmetric.AES;
//import cn.hutool.http.HttpRequest;
//import cn.hutool.http.HttpUtil;
//import com.cy.data.ImoocBaseData;
//import com.cy.decrypt.AESCBCDecrypt;
//import com.cy.decrypt.ImoocDecoder;
//import com.cy.model.DownloadItem;
//import com.google.gson.Gson;
//import com.iheartradio.m3u8.*;
//import com.iheartradio.m3u8.data.Playlist;
//import imooc.m3u8.model.M3U8;
//import imooc.m3u8.model.M3U8PlayInfo;
//import imooc.m3u8.parse.ParseM3U8;
//import imooc.pojo.ajax.media.info.AjaxMediaInfo;
//import imooc.pojo.ajax.media.info.MediaInfo;
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import top.cyblogs.data.HttpData;
//import top.cyblogs.util.JacksonUtils;
//
//import javax.crypto.Cipher;
//import java.io.*;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * 下载单项
// * 该类对获取到的一个完整的视频文件进行分段的下载、分段的解密、全段的合并 2019年7月10日
// *
// * @author CY
// */
//public class WrapDownloadInfo {
//    /**
//     * 下载一个文件 OK
//     */
//    public void download(DownloadItem downloadItem) {
//        if ("video".equals(downloadItem.getType())) {
//            downloadVideo(downloadItem);
//        } else if ("code".equals(downloadItem.getType())) {
//            downloadCode(downloadItem);
//        } else if ("ceping".equals(downloadItem.getType())) {
//            downloadCeping(downloadItem);
//        } else if ("article".equals(downloadItem.getType())) {
//            downloadArticle(downloadItem);
//        } else if ("download".equals(downloadItem.getType())) {
//            downloadResource(downloadItem);
//        }
//    }
//
//    /**
//     * 下载文章 OK
//     */
//    private void downloadArticle(DownloadItem downloadItem) {
//        // 如果文件存在，跳过
//        File outputFile = downloadItem.getOutputFile();
//        if (outputFile.exists()) {
//            return;
//        }
//        try {
//            FileUtil.mkParentDirs(outputFile);
//            PrintStream printStream = new PrintStream(outputFile);
//            // 获取JSON
//            Connection connection = Jsoup.connect(downloadItem.getUrl()).cookie("apsid", ImoocBaseData.apsid);
//            connection.header("Referer", ImoocBaseData.CODING_HOME_URL);
//            connection.header("X-Requested-With", "XMLHttpRequest");
//            String json = connection.get().text();
//            AjaxMediaInfo ajaxMediaInfo = new Gson().fromJson(json, AjaxMediaInfo.class);
//            MediaInfo mediaInfo = ajaxMediaInfo.getData().getMediaInfo();
//            // 写文件
//            if (mediaInfo != null) {
//                printStream.println("<h3>" + mediaInfo.getProgramName() + "</h3>");
//                printStream.println(mediaInfo.getProgramDescription());
//                printStream.println(mediaInfo.getProgramTask());
//            }
//            printStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 下载测评 OK
//     */
//    private void downloadCeping(DownloadItem downloadItem) {
//        // 如果文件存在，跳过
//        File outputFile = downloadItem.getOutputFile();
//        if (outputFile.exists()) {
//            return;
//        }
//        try {
//            FileUtil.mkParentDirs(outputFile);
//            PrintStream printStream = new PrintStream(outputFile);
//            Document document = Jsoup.connect(downloadItem.getUrl()).cookie("apsid", ImoocBaseData.apsid).get();
//            // 写文件
//            printStream.println(document.select("#courseCenter").toString());
//            printStream.println(document.select(".examOption").toString());
//            printStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 下载代码 OK
//     */
//    private void downloadCode(DownloadItem downloadItem) {
//        // 如果文件存在，跳过
//        File outputFile = downloadItem.getOutputFile();
//        if (outputFile.exists()) {
//            return;
//        }
//        try {
//            FileUtil.mkParentDirs(outputFile);
//            PrintStream printStream = new PrintStream(outputFile);
//            Document document = Jsoup.connect(downloadItem.getUrl()).header("Cookie", ImoocBaseData.cookie).get();
//
//            // 写文件
//            printStream.println(document.select("#js-aticle-container").toString());
//            printStream.println("<h3>代码</h3>");
//            printStream.println(document.select("div[data-filename]").toString());
//            printStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 下载资料文件 OK
//     */
//    private void downloadResource(DownloadItem downloadItem) {
//        // 如果文件存在，跳过
//        File outputFile = downloadItem.getOutputFile();
//        if (outputFile.exists()) {
//            return;
//        }
//        try {
//            InputStream inputStream = new URL(downloadItem.getUrl()).openStream();
//
//            // 写入文件
//            byte[] bytes = IoUtil.readBytes(inputStream);
//            FileUtil.mkParentDirs(outputFile);
//            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
//            fileOutputStream.write(bytes);
//            fileOutputStream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 下载视频文件 OK
//     */
//    private void downloadVideo(DownloadItem downloadItem) throws ParseException, PlaylistException, IOException {
//        File outputFile = downloadItem.getOutputFile();
//        if (outputFile.exists()) {
//            return;
//        }
//        // 得到第一个清晰度，一般第一个清晰度是最好的
//        String url = downloadItem.getUrl();
//        InputStream quality = IoUtil.toStream(ImoocDecoder.decoderString(info(url)), "UTF-8");
//        String m3u8Url = new PlaylistParser(quality, Format.EXT_M3U, Encoding.UTF_8).parse().getMediaPlaylist().getTracks().get(0).getUri();
//        // 获取该视频的m3u8对象
//        InputStream m3u8 = IoUtil.toStream(ImoocDecoder.decoderString(info(m3u8Url)), "UTF-8");
//        PlaylistParser playlistParser = new PlaylistParser(m3u8, Format.EXT_M3U, Encoding.UTF_8);
//        Playlist playlist = playlistParser.parse();
//        String encryptUri = playlist.getMediaPlaylist().getTracks().get(0).getEncryptionData().getUri();
//        // 获取M3U8的密钥
//        byte[] key = ImoocDecoder.decoderKey(info(encryptUri));
//        // 获取视频片段列表
//        List<M3U8PlayInfo> m3u8PlayList = m3u8.getM3u8PlayList();
//
//        // 遍历片段列表
//        ArrayList<File> tempFiles = new ArrayList<>();
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        CountDownLatch countDownLatch = new CountDownLatch(m3u8PlayList.size());
//        for (int i = 0; i < m3u8PlayList.size(); i++) {
//            // 构建临时文件
//            File file = new File(ApplicationInfo.APP_TEMP_FILE_PATH + downloadItem.getFileName() + File.separator + i + ".ts");
//            tempFiles.add(file);
//            if (file.exists()) {
//                currentPart.incrementAndGet();
//                countDownLatch.countDown();
//                continue;
//            }
//            createDirections(file);
//            int order = i;
//            executorService.submit(() -> {
//                // 下载这一段ts文件
//                try (FileOutputStream outputStream = new FileOutputStream(file)) {
//                    downloadPartFile(m3u8PlayList, order, key, outputStream);
//                    currentPart.incrementAndGet();
//                } catch (IOException e) {
//                    new Thread(() -> UsageRecord.getInstance().appendReportInfo(e.getMessage())).start();
//                } finally {
//                    countDownLatch.countDown();
//                }
//            });
//        }
//        executorService.shutdown();
//
//        // 网络速度
//        AtomicLong tempCurrentBytes = new AtomicLong(0);
//        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//            tempCurrentBytes.set(currentBytes.get());
//        }, 0, 1, TimeUnit.SECONDS);
//        // 等待进行合并视频
//        try {
//            countDownLatch.await();
//            scheduledExecutorService.shutdownNow();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 得到一小段ts视频解密后的字节数组 OK
//     *
//     * @param url 下载地址
//     * @param key 密钥
//     * @param iv  IV
//     * @return 解密后的字节数组
//     */
//    private byte[] decodeVideo(String url, byte[] key, byte[] iv) {
//        try (InputStream inputStream = new URL(url).openConnection().getInputStream()) {
//            byte[] bytes = IoUtil.readBytes(inputStream);
//            return new AES(Mode.CBC, Padding.PKCS5Padding, key, iv).decrypt(bytes);
//        } catch (Exception e) {
//            decodeVideo(url, key, iv);
//        }
//        return null;
//    }
//
//    /**
//     * 根据请求的URL获取响应的JSON，并且获取到info信息 OK
//     *
//     * @param url 请求的URL
//     * @return info信息
//     */
//    private String info(String url) {
//        HttpRequest request = HttpUtil.createGet(url).timeout(60000);
//        request.header("User-Agent", HttpData.USER_AGENT);
//        request.cookie(ImoocBaseData.cookie);
//        String json = request.execute().body();
//        return JacksonUtils.toJsonNode(json).get("data").get("info").asText();
//
//    }
//
//    /**
//     * 得到文件的IV OK
//     *
//     * @param i 一个数字
//     * @return 一个IV值
//     */
//    private byte[] iv(byte i) {
//        return new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, i};
//    }
//}