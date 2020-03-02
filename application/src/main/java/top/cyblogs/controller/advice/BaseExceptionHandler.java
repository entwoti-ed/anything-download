package top.cyblogs.controller.advice;

import com.cy.exception.AbstractResultException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.cyblogs.model.response.BaseResponse;

/**
 * 统一异常处理类
 *
 * @author CY
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 处理程序的异常
     */
    @ExceptionHandler(value = AbstractResultException.class)
    public BaseResponse<Object> error(AbstractResultException e) {
        return new BaseResponse<>(false, e.getStatus(), e.getMessage(), null);
    }

    /**
     * 处理全局的异常
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse<Object> handleGlobalException(Exception e) {
        e.printStackTrace();
        return new BaseResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
    }
}
