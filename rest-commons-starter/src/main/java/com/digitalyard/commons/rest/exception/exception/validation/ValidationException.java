package com.digitalyard.commons.rest.exception.exception.validation;

import com.digitalyard.commons.rest.exception.model.ErrorCode;
import com.digitalyard.commons.rest.exception.exception.AbstractApiException;

import java.util.Map;

/**
 * Базовый класс для всех исключений, связанных с валидацией данных.
 */
public abstract class ValidationException extends AbstractApiException {
    protected ValidationException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    protected ValidationException(ErrorCode errorCode, Map<String, Object> details, Throwable cause) {
        super(errorCode, details, cause);
    }
}