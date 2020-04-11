package top.cyblogs.listener;

/**
 * 下载工具类的回调
 * 2019年7月13日
 *
 * @author CY
 */
public class DownloadListener {

    /**
     * 正在连接下载资源
     *
     * @param url 正在连接的URL
     */
    public void connecting(String url) {
    }

    /**
     * 开始下载
     *
     * @param length 文件总大小
     *               如果文件大小为-1则是响应流文件
     *               如果此时要使用进度条，请使用没有进度的进度条
     */
    public void start(long length) {
    }

    /**
     * 下载中
     *
     * @param progress 当前进度
     * @param speed    下载的实时速度
     * @param time     剩余时长 单位S
     *                 剩余时长仅供参考，计算方法（总长度 - 当前长度） / 当前速度
     *                 如果返回-1则代表无法计算剩余时长
     */
    public void downloading(double progress, long speed, long time) {
    }

    /**
     * 下载完成
     *
     * @param time 下载总时长 单位S
     *             注意这里的下载总时长没有计算连接中所花费的时间
     */
    public void over(long time) {
    }

    /**
     * 下载错误的回调
     *
     * @param e 下载出现的异常
     *          请注意大部分情况e参数为null
     *          如果e参数为null，说明是URL验证错误
     *          如果e参数不为null的话说明是下载中错误，此时需要进行异常的处理
     */
    public void downloadError(Exception e) {
    }
}