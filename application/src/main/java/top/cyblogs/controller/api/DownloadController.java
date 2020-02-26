package top.cyblogs.controller.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cyblogs.model.params.AddDownload;
import top.cyblogs.model.response.BaseResponse;
import top.cyblogs.service.ApplicationService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author CY
 */
@RestController
@RequestMapping("download")
@CrossOrigin(origins = "*")
public class DownloadController {

    @Resource
    private ApplicationService downloadService;

    @RequestMapping("addDownload")
    public BaseResponse<String> addDownload(@Valid @RequestBody AddDownload download) {
        System.out.println(download);
        downloadService.download(download);
        return BaseResponse.ok("正在给你添加下载呢!!!");
    }
}
