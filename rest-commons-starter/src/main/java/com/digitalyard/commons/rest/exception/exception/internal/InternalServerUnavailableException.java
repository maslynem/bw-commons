package com.digitalyard.commons.rest.exception.exception.internal;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;

/**
 * Исключение для внутренних ошибок сервера.
 */
public class InternalServerUnavailableException extends InternalServerErrorException {
    public static final String SERVICE_NAME = "serviceName";

    public InternalServerUnavailableException(String serviceName) {
        super(CommonErrorCode.SERVICE_UNAVAILABLE, Map.of(SERVICE_NAME, serviceName));
    }
}