package com.cy.exception;

import org.springframework.http.HttpStatus;

/**
 * 资源禁止访问
 *
 * @author CY
 */
public class ForbiddenException extends AbstractResultException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
