package top.cyblogs.service;

import org.springframework.stereotype.Service;
import top.cyblogs.exception.UrlNotSupportException;
import top.cyblogs.model.enums.SupportUrl;
import top.cyblogs.model.params.AddDownload;
import top.cyblogs.support.BiliBiliSupport;

/**
 * 应用总服务
 *
 * @author CY
 */
@Service
public class ApplicationService {

    public void download(AddDownload download) {
        String url = download.getUrl();
        if (url.contains(SupportUrl.BILIBILI.getUrlContains())) {
            BiliBiliSupport.start(url, download.getCookie());
        } else {
            throw new UrlNotSupportException("您要下载的URL还没有被支持, 请查看支持列表");
        }
    }
}
