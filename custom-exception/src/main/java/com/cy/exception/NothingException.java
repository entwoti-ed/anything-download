package com.cy.exception;

/**
 * 啥都没有异常
 *
 * @author CY
 */
public class NothingException extends NotFoundException {

    public NothingException(String message) {
        super(message);
    }

    public NothingException(String message, Throwable cause) {
        super(message, cause);
    }
}