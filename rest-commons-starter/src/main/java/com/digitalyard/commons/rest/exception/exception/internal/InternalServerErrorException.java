package com.digitalyard.commons.rest.exception.exception.internal;

import com.digitalyard.commons.rest.exception.exception.AbstractApiException;
import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;

/**
 * Исключение для внутренних ошибок сервера.
 */
public class InternalServerErrorException extends AbstractApiException {
    public static final String THROWABLE = "throwable";

    public InternalServerErrorException(Throwable throwable) {
        super(CommonErrorCode.INTERNAL_ERROR, Map.of(THROWABLE, throwable));
    }

    public InternalServerErrorException(CommonErrorCode commonErrorCode, Map<String, Object> details) {
        super(commonErrorCode, details);
    }


}