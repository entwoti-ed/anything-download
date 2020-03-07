package top.cyblogs.controller.websocket;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import top.cyblogs.data.DownloadList;
import top.cyblogs.download.DownloadUtils;
import top.cyblogs.model.dto.StatusDTO;
import top.cyblogs.output.Aria2cGlobalStat;

import javax.annotation.Resource;

/**
 * 该类用来每秒向前台发送一次下载状态的list集合
 *
 * @author CY
 */
@Slf4j
@RestController
public class DownloadSocketController {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 每秒钟都要向前台发送一次全局状态
     */
    @Scheduled(cron = "*/1 * * * * ?")
    public void globalStatus() {
        if (DownloadList.list.size() == 0) {
            return;
        }
        Aria2cGlobalStat aria2cGlobalStat = DownloadUtils.globalStatus();
        String downloadSpeed = FileUtil.readableFileSize(aria2cGlobalStat.getDownloadSpeed()) + "/S";
        messagingTemplate.convertAndSend("/downloadStatus/notice", new StatusDTO(downloadSpeed, DownloadList.list));
    }
}
