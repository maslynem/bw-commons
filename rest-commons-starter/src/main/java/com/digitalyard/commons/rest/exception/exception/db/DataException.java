package com.digitalyard.commons.rest.exception.exception.db;

import com.digitalyard.commons.rest.exception.model.ErrorCode;
import com.digitalyard.commons.rest.exception.exception.AbstractApiException;

import java.util.Map;

/**
 * Базовый класс для всех исключений, связанных с операциями с данными.
 */
public abstract class DataException extends AbstractApiException {
    protected DataException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    protected DataException(ErrorCode errorCode, Map<String, Object> details, Throwable cause) {
        super(errorCode, details, cause);
    }
}