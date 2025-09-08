package ru.boardworld.commons.rest.exception.logger;

import ru.boardworld.commons.rest.exception.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;

public interface ApiErrorLogger {
    void logError(ApiError apiError, Exception exception, HttpServletRequest request);
}
