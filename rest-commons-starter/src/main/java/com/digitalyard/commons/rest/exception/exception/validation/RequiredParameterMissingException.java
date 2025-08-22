package com.digitalyard.commons.rest.exception.exception.validation;
import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;

/**
 * Исключение для отсутствующих обязательных параметров.
 */
public class RequiredParameterMissingException extends ValidationException {
    public static final String PARAMETER_NAME = "parameterName";

    public RequiredParameterMissingException(String parameterName) {
        super(CommonErrorCode.REQUIRED_PARAMETER_MISSING,
                Map.of(PARAMETER_NAME, parameterName));
    }

}