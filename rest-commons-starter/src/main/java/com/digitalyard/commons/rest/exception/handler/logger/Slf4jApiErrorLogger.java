package com.digitalyard.commons.rest.exception.handler.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.digitalyard.commons.rest.exception.model.ApiError;
import com.digitalyard.commons.rest.exception.model.ErrorCode;
import com.digitalyard.commons.rest.exception.resolver.ErrorMessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class Slf4jApiErrorLogger implements ApiErrorLogger {
    private final ErrorMessageResolver errorMessageResolver;
    private final ObjectMapper objectMapper;

    @Override
    public void logError(ApiError apiError) {
        try {
            ErrorCode errorCode = apiError.getCode();
            Map<String, Object> details = apiError.getDetails();
            String logMessage = errorMessageResolver.resolve(errorCode, details);

            // Создаем структурированный объект для логирования
            Map<String, Object> logEntry = new LinkedHashMap<>();
            logEntry.put("message", logMessage);
            logEntry.put("error", apiError);

            log.error("API Error: {}", objectMapper.writeValueAsString(logEntry));

        } catch (Exception ex) {
            log.error("Error during logging ApiError with id {}", apiError.getId());
            log.error("", ex);
        }
    }

}
