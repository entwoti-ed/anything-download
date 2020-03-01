package top.cyblogs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import top.cyblogs.model.enums.ServiceType;

import java.io.File;
import java.util.Map;

@Accessors(chain = true)
@AllArgsConstructor
@Data
public class TempDownloadItem {

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
     * 目标文件
     */
    @JsonIgnore
    private File targetFile;

    /**
     * 头信息
     */
    @JsonIgnore
    private Map<String, String> header;

    /**
     * 初始化下载状态
     */
    @JsonIgnore
    private DownloadItem initialStatus;
}
