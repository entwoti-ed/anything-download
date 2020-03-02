package com.cy.exception;

/**
 * 参数格式不正确
 *
 * @author CY
 */
public class PropertyFormatException extends BadRequestException {

    public PropertyFormatException(String message) {
        super(message);
    }

    public PropertyFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
