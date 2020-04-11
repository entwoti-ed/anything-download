package com.cy.download;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import top.cyblogs.listener.DownloadListener;
import top.cyblogs.utils.DownloadUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;

public class TestDownload {

    public static void main(String[] args) {
        HashMap<String, String> header = new HashMap<>();

        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        header.put("Referer", "https://www.bilibili.com/");

        DownloadUtils.download("https://cn-nmghhht-cu-v-03.bilivideo.com/upgcxcode/94/03/175820394/175820394-1-30064.m4s?expires=1586606400&platform=pc&ssig=s0P9hDwqG-7CUrD-i9m1Zg&oi=1021207770&trid=d59f089532524b83a2eb689b7c0df50du&nfc=1&nfb=maPYqpoel5MI3qOUX6YpRA==&mid=10521774&logo=80000000",
                new File("C:\\Users\\dnydi\\Desktop\\下载目录\\QQ.mp4"), header,
                new DownloadListener() {

                    DecimalFormat percentFormat = new DecimalFormat("0.00");

                    @Override
                    public void connecting(String url) {
                        System.out.println("连接中: " + url);
                    }

                    @Override
                    public void start(long length) {
                        System.out.println("下载已经开始, 文件大小为: " + FileUtil.readableFileSize(length));
                    }

                    @Override
                    public void downloading(double progress, long speed, long time) {
                        System.out.println(String.format("下载中, 当前进度: %s%%, 速度为: %s/S, 剩余时间为: %sS",
                                percentFormat.format(progress * 100),
                                FileUtil.readableFileSize(speed),
                                time));
                    }

                    @Override
                    public void over(long time) {
                        System.out.println("下载完成...耗时: " + DateUtil.formatBetween(time));
                    }

                    @Override
                    public void downloadError(Exception e) {
                        System.out.println("异常信息: " + e.getMessage());
                    }
                });
    }
}
