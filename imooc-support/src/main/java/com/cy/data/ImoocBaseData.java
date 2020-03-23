package com.cy.data;

import java.io.File;

/**
 * 基础应用数据
 *
 * @author CY
 */
public class ImoocBaseData {

    /**
     * 慕课网主页
     */
    public static final String HOME_URL = "https://www.imooc.com";
    /**
     * 实战课程主页
     */
    public static final String CODING_HOME_URL = "https://coding.imooc.com";
    /**
     * 免费课程列表的基本URL
     */
    public static final String FREE = "www.imooc.com/learn";
    /**
     * 试看课程列表的基本URL
     */
    public static final String TRY = "coding.imooc.com/class/chapter";
    /**
     * 收费课程列表的基本URL
     */
    public static final String PAY = "coding.imooc.com/learn/list";

    /**
     * 开启线程的数量，默认为5
     */
    public static int threadNum = 5;
    /**
     * 文件默认保存的路径
     */
    public static String path = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;
    /**
     * 默认下载的视频格式
     */
    public static String videoFormat = ".mp4";
    /**
     * 是否下载文本
     */
    public static boolean isDownloadText = true;

    /**
     * 保持用户的登陆状态，默认给一个Cookie，用来下载免费课程的资料
     */
    public static String cookie = "apsid=AzZjJkYTlhNjI4ODI0M2UxZjMzYWRjNTNjMjI0ZmEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANjkzMzY2NgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABUaWdlcldhbmc5N0AxNjMuY29tAAAAAAAAAAAAAAAAAGIyNDk3M2M4YzVlZWM0YzM1M2NiZmVkYWY5N2MyNDdlbXwfXW18H10%3DNz";
}