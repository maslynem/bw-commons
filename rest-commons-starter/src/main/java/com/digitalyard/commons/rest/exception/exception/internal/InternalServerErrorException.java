package com.digitalyard.commons.rest.exception.exception.internal;

import com.digitalyard.commons.rest.exception.exception.AbstractApiException;
import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;

/**
 * Исключение для внутренних ошибок сервера.
 */
public abstract class InternalServerErrorException extends AbstractApiException {

    public InternalServerErrorException(CommonErrorCode commonErrorCode, Map<String, Object> details) {
        super(commonErrorCode, details);
    }


}