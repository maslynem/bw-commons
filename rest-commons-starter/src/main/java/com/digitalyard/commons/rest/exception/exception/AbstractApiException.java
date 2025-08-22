package com.digitalyard.commons.rest.exception.exception;

import com.digitalyard.commons.rest.exception.model.ErrorCode;
import lombok.Getter;

import java.util.Map;

/**
 * Абстрактное базовое исключение для всех кастомных исключений в системе.
 * Содержит только ErrorCode и детали, но не HTTP статус.
 */
@Getter
public abstract class AbstractApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    protected AbstractApiException(ErrorCode errorCode, Map<String, Object> details, Throwable cause) {
        super(generateDebugMessage(errorCode, details, cause), cause);
        this.errorCode = errorCode;
        this.details = details;
    }

    protected AbstractApiException(ErrorCode errorCode, Map<String, Object> details) {
        this(errorCode, details, null);
    }

    protected AbstractApiException(ErrorCode errorCode) {
        this(errorCode, Map.of(), null);
    }

    private static String generateDebugMessage(ErrorCode errorCode, Map<String, Object> details, Throwable cause) {
        StringBuilder message = new StringBuilder("ErrorCode: ").append(errorCode.getCode());

        if (!details.isEmpty()) {
            message.append(", Details: ").append(details);
        }

        if (cause != null) {
            message.append(", Cause: ").append(cause.getMessage());
        }

        return message.toString();
    }

}