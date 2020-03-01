package top.cyblogs.controller.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cyblogs.model.TempDownloadItem;
import top.cyblogs.model.params.AddDownload;
import top.cyblogs.model.params.NormalDownload;
import top.cyblogs.model.response.BaseResponse;
import top.cyblogs.service.ApplicationService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author CY
 */
@RestController
@RequestMapping("download")
@CrossOrigin(origins = "*")
public class DownloadController {

    @Resource
    private ApplicationService downloadService;

    @RequestMapping("downloadList")
    public BaseResponse<List<TempDownloadItem>> downloadList(@Valid @RequestBody AddDownload download) {
        List<TempDownloadItem> tempDownloadList = downloadService.download(download);
        return BaseResponse.ok("下载列表获取成功!", tempDownloadList);
    }

    @RequestMapping("startDownload")
    public BaseResponse<String> startDownload(@RequestBody List<String> downloadList) {
        downloadService.startDownload(downloadList);
        return BaseResponse.ok("开始了...");
    }

    @RequestMapping("normalDownload")
    public BaseResponse<String> normalDownload(@Valid @RequestBody NormalDownload normalDownload) {
        downloadService.normalDownload(normalDownload);
        return BaseResponse.ok("开始了...");
    }
}
