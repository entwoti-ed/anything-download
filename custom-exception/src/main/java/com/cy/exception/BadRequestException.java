package com.cy.exception;

import org.springframework.http.HttpStatus;

/**
 * 请求参数错误
 *
 * @author CY
 */
public class BadRequestException extends AbstractResultException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
