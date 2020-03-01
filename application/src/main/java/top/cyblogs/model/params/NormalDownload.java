package top.cyblogs.model.params;

import lombok.Data;
import top.cyblogs.model.enums.DownloadType;
import top.cyblogs.model.enums.ServiceType;

import javax.validation.constraints.NotNull;

/**
 * 普通的下载
 *
 * @author CY
 */
@Data
public class NormalDownload {

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private DownloadType downloadType;

    private String url;

    private String[] urls;

    @NotNull
    private String title;

    @NotNull
    private String cookie;

    @NotNull
    private String source;
}
