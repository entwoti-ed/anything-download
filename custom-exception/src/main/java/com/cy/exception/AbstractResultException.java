package com.cy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * 项目的基础异常
 *
 * @author CY
 */
public abstract class AbstractResultException extends RuntimeException {

    AbstractResultException(String message) {
        super(message);
    }

    AbstractResultException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    public abstract HttpStatus getStatus();
}
