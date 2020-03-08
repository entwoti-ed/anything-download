package top.cyblogs.exception;

import org.springframework.http.HttpStatus;

/**
 * 下载中...
 *
 * @author CY
 */
public class DownloadBusyException extends AbstractResultException {

    public DownloadBusyException(String message) {
        super(message);
    }

    public DownloadBusyException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
