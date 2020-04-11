package top.cyblogs.controller.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.cyblogs.model.dto.TempDownloadListDTO;
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
    public BaseResponse<TempDownloadListDTO> downloadList(@Valid @RequestBody AddDownload download) {
        TempDownloadListDTO downloadListDTO = downloadService.download(download);
        return BaseResponse.ok("下载列表获取成功!", downloadListDTO);
    }

    @RequestMapping("startDownload")
    public BaseResponse<String> startDownload(String listId, @RequestBody List<String> downloadList) {
        downloadService.startDownload(listId, downloadList);
        return BaseResponse.ok("开始了...");
    }

    @RequestMapping("normalDownload")
    public BaseResponse<String> normalDownload(@Valid @RequestBody NormalDownload normalDownload) {
        downloadService.normalDownload(normalDownload);
        return BaseResponse.ok("开始了...");
    }

    @RequestMapping("cleanTemp")
    public BaseResponse<String> cleanTemp() {
        downloadService.cleanTemp();
        return BaseResponse.ok("清理缓存文件成功!");
    }
}
