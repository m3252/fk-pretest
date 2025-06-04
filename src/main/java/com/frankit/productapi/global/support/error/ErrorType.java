package com.frankit.productapi.global.support.error;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import static com.frankit.productapi.global.support.error.ErrorCode.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public enum ErrorType {
    DEFAULT_ERROR(INTERNAL_SERVER_ERROR, E500, "An unexpected error has occurred", LogLevel.ERROR),
    USER_NOT_FOUND_ERROR(BAD_REQUEST, E404, "User not found", LogLevel.WARN),
    UNAUTHORIZED_ERROR(BAD_REQUEST, E401, "Unauthorized access", LogLevel.WARN),
    INVALID_REQUEST(BAD_REQUEST, E400, "Invalid request", LogLevel.WARN),
    USER_ALREADY_EXISTS_ERROR(BAD_REQUEST, E400, "User already exists", LogLevel.WARN),
    PRODUCT_NOT_FOUND_ERROR(BAD_REQUEST, E400, "Product not found", LogLevel.WARN),
    PRODUCT_OPTION_NOT_FOUND_ERROR(BAD_REQUEST, E400, "Product option not found", LogLevel.WARN),
    INVALID_OPTION_TYPE(BAD_REQUEST, E400, "Invalid option type", LogLevel.WARN),
    ;

    private final HttpStatus status;

    private final ErrorCode code;

    private final String message;

    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode code, String message, LogLevel logLevel) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }
}
