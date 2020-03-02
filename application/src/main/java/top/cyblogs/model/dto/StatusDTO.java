package top.cyblogs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.cyblogs.model.DownloadItem;

import java.util.List;

/**
 * 状态类型
 *
 * @author CY
 */
@Data
@AllArgsConstructor
public class StatusDTO {

    private String globalSpeed;

    private List<DownloadItem> downloadList;
}
