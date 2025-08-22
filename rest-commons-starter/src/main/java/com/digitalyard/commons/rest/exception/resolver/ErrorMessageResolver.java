package com.digitalyard.commons.rest.exception.resolver;

import com.digitalyard.commons.rest.exception.model.ErrorCode;

import java.util.Locale;
import java.util.Map;

/**
 * Интерфейс для разрешения сообщений об ошибках на основе ErrorCode и деталей.
 */
public interface ErrorMessageResolver {


    String resolve(ErrorCode errorCode, Map<String, Object> details);


    String resolve(ErrorCode errorCode, Map<String, Object> details, Locale locale);


    default String resolve(ErrorCode errorCode) {
        return resolve(errorCode, Map.of());
    }


    default String resolve(ErrorCode errorCode, Locale locale) {
        return resolve(errorCode, Map.of(), locale);
    }
}