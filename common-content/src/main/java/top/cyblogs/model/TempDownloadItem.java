package top.cyblogs.model;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import top.cyblogs.model.enums.ServiceType;

import java.io.File;
import java.util.Map;

@Data
public class TempDownloadItem {

    /**
     * URL
     */
    @JsonIgnore
    private String url;
    /**
     * URL列表
     */
    @JsonIgnore
    private String[] urls;
    /**
     * 初始化下载状态
     */
    @JsonIgnore
    private DownloadItem downloadStatus;

    public static TempDownloadItem init(String url, File targetFile, ServiceType serviceType, Map<String, String> header, DownloadItem downloadStatus) {
        TempDownloadItem item = initAndSetProperties(targetFile, serviceType, header, downloadStatus);
        item.url = url;
        return item;
    }

    public static TempDownloadItem init(String[] urls, File targetFile, ServiceType serviceType, Map<String, String> header, DownloadItem downloadStatus) {
        TempDownloadItem item = initAndSetProperties(targetFile, serviceType, header, downloadStatus);
        item.urls = urls;
        return item;
    }

    /**
     * 下载ID
     */
    private String downloadId;

    /**
     * 文件的名字
     */
    private String fileName;

    /**
     * 下载类型
     */
    @JsonIgnore
    private ServiceType serviceType;

    /**
     * 目标文件
     */
    @JsonIgnore
    private File targetFile;

    /**
     * 头信息
     */
    @JsonIgnore
    private Map<String, String> header;

    private static TempDownloadItem initAndSetProperties(File targetFile, ServiceType serviceType, Map<String, String> header, DownloadItem downloadStatus) {
        TempDownloadItem item = new TempDownloadItem();
        item.downloadId = SecureUtil.md5(FileUtil.getCanonicalPath(targetFile));
        item.fileName = targetFile.getName();
        item.serviceType = serviceType;
        item.targetFile = targetFile;
        item.header = header;
        item.downloadStatus = downloadStatus;
        return item;
    }
}
