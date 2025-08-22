package com.digitalyard.commons.rest.exception.exception.validation;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;

/**
 * Исключение для недопустимых значений параметров.
 */
public class InvalidParameterValueException extends ValidationException {
    public static final String PARAMETER_NAME = "parameterName";
    public static final String VALUE = "value";
    public static final String EXPECTED = "expected";

    public InvalidParameterValueException(String parameterName, Object value) {
        super(CommonErrorCode.INVALID_PARAMETER_VALUE,
                Map.of(PARAMETER_NAME, parameterName, VALUE, value));
    }

    public InvalidParameterValueException(String parameterName, Object value, Object expected) {
        super(CommonErrorCode.INVALID_PARAMETER_VALUE,
                Map.of(PARAMETER_NAME, parameterName, VALUE, value, EXPECTED, expected));
    }

    public InvalidParameterValueException(Map<String, Object> details) {
        super(CommonErrorCode.INVALID_PARAMETER_VALUE, details);
    }

    public InvalidParameterValueException(Map<String, Object> details, Throwable throwable) {
        super(CommonErrorCode.INVALID_PARAMETER_VALUE, details, throwable);
    }
}