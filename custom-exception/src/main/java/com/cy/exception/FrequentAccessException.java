package com.cy.exception;

/**
 * 访问过于频繁
 *
 * @author CY
 */
public class FrequentAccessException extends BadRequestException {

    public FrequentAccessException(String message) {
        super(message);
    }

    public FrequentAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
