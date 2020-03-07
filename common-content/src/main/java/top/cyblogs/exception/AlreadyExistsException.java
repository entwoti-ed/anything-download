package top.cyblogs.exception;

/**
 * 实体已经存在，添加数据时使用
 *
 * @author CY
 */
public class AlreadyExistsException extends BadRequestException {

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
