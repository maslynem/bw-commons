package com.digitalyard.commons.rest.exception.exception.validation;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;

/**
 * Общее исключение для ошибок валидации.
 * Переименовано из ValidationException в GeneralValidationException для ясности.
 */
public class GeneralValidationException extends ValidationException {
    public static final String FIELD = "field";
    public static final String VALUE = "value";
    public static final String REASON = "reason";

    public GeneralValidationException(Map<String, Object> details) {
        super(CommonErrorCode.VALIDATION_FAILED, details);
    }

    public GeneralValidationException(Map<String, Object> details, Throwable throwable) {
        super(CommonErrorCode.VALIDATION_FAILED, details, throwable);
    }

    public GeneralValidationException(String fieldName, Object invalidValue) {
        super(CommonErrorCode.VALIDATION_FAILED,
                Map.of(FIELD, fieldName, VALUE, invalidValue));
    }

    public GeneralValidationException(String fieldName, Object invalidValue, String reason) {
        super(CommonErrorCode.VALIDATION_FAILED,
                Map.of(FIELD, fieldName, VALUE, invalidValue, REASON, reason));
    }
}