package com.digitalyard.commons.rest.exception.handler;

import com.digitalyard.commons.rest.exception.model.ApiError;
import com.digitalyard.commons.rest.exception.model.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Фабрика для создания объектов ApiError.
 * Используется для централизованного создания ошибок, в том числе в обработчиках Spring Security.
 */
@RequiredArgsConstructor
public class ApiErrorFactory {

    public ApiError create(ErrorCode errorCode, HttpStatus httpStatus, Map<String, Object> details) {
        return ApiError.builder()
                .id(UUID.randomUUID())
                .status(httpStatus.value())
                .code(errorCode)
                .timestamp(Instant.now())
                .details(details)
                .build();
    }

    public ApiError create(ErrorCode errorCode, HttpStatus httpStatus) {
        return create(errorCode, httpStatus, Map.of());
    }

}