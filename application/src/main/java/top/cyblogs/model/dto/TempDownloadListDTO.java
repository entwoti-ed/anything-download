package top.cyblogs.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.cyblogs.model.TempDownloadItem;

import java.util.List;

@Data
@AllArgsConstructor
public class TempDownloadListDTO {

    private String listId;

    private List<TempDownloadItem> list;
}
