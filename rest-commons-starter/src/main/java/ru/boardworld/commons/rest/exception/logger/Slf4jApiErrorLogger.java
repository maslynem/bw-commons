package ru.boardworld.commons.rest.exception.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.boardworld.commons.rest.exception.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class Slf4jApiErrorLogger implements ApiErrorLogger {
    private final ObjectMapper objectMapper;

    @Override
    public void logError(ApiError apiError, Exception exception, HttpServletRequest request) {
        try {
            Map<String, Object> logEntry = new LinkedHashMap<>();
            logEntry.put("error", apiError);
            logEntry.put("method", request.getMethod());
            logEntry.put("exception_name", exception.getClass().getSimpleName());
            logEntry.put("exception_message", exception.getMessage());
            logEntry.put("url", request.getRequestURI());
            logEntry.put("query", request.getQueryString());
            logEntry.put("remoteAddr", request.getRemoteAddr());

            if (apiError.getStatus() >= 500) {
                log.error("API Error: {}", objectMapper.writeValueAsString(logEntry));
            } else {
                log.warn("API Error: {}", objectMapper.writeValueAsString(logEntry));
            }
        } catch (Exception ex) {
            log.error("Error during logging ApiError with id {}", apiError.getId());
            log.error(ex.getMessage());
        }
    }
}
