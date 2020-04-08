package top.cyblogs.api;

import cn.hutool.core.io.IoUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTest {

    public static void main(String[] args) throws IOException {
        URL url = new URL("https://upos-sz-mirrorcos.bilivideo.com/upgcxcode/21/64/174916421/174916421-1-30080.m4s?e=ig8euxZM2rNcNbdlhoNvNC8BqJIzNbfqXBvEqxTEto8BTrNvN0GvT90W5JZMkX_YN0MvXg8gNEV4NC8xNEV4N03eN0B5tZlqNxTEto8BTrNvNeZVuJ10Kj_g2UB02J0mN0B5tZlqNCNEto8BTrNvNC7MTX502C8f2jmMQJ6mqF2fka1mqx6gqj0eN0B599M=&uipk=5&nbs=1&deadline=1586251241&gen=playurl&os=cosbv&oi=1021237460&trid=a74d377eefc2483b833866a60d4c09a5u&platform=pc&upsig=e7a40e86547564ed1121bc4523482d20&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=10521774&logo=80000000");
        URLConnection connection = url.openConnection();
        connection.addRequestProperty("Referer", "https://www.bilibili.com/video/BV17a4y1x7zq?p=7");
        InputStream inputStream = connection.getInputStream();
        IoUtil.copy(inputStream, new FileOutputStream("C:\\Users\\dnydi\\Desktop\\aria2\\临时下载.m4s"));
    }
}
