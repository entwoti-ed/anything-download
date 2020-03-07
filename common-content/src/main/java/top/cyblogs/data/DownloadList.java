package top.cyblogs.data;

import top.cyblogs.model.DownloadItem;
import top.cyblogs.model.TempDownloadItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放下载的数据
 *
 * @author CY
 */
public class DownloadList {

    /**
     * 存放下载列表，用来实时的推送下载状态到前端
     */
    public transient static List<DownloadItem> list = new ArrayList<>();

    /**
     * 临时下载列表，用于展示到前端供用户选择
     */
    public transient static List<TempDownloadItem> tempList = new ArrayList<>();
}
