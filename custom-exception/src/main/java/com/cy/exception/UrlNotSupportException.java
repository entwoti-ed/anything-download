package com.cy.exception;

public class UrlNotSupportException extends BadRequestException {

    public UrlNotSupportException(String message) {
        super(message);
    }

    public UrlNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }
}
