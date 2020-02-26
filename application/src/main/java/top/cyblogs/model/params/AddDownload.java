package top.cyblogs.model.params;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 添加下载
 *
 * @author CY
 */
@Data
@Accessors(chain = true)
public class AddDownload {

    @NotBlank
    private String url;

    private String cookie;
}
