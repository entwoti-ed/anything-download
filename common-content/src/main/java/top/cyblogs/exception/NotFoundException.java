package top.cyblogs.exception;

import org.springframework.http.HttpStatus;

/**
 * 未查询到结果
 *
 * @author CY
 */
public class NotFoundException extends AbstractResultException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
