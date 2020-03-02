package com.cy.exception;

/**
 * 网络异常
 *
 * @author CY
 */
public class InternetException extends BadRequestException {

    public InternetException(String message) {
        super(message);
    }

    public InternetException(String message, Throwable cause) {
        super(message, cause);
    }

}