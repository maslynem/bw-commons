package com.digitalyard.commons.rest.exception.logger;

import com.digitalyard.commons.rest.exception.model.ApiError;
import jakarta.servlet.http.HttpServletRequest;

public interface ApiErrorLogger {
    void logError(ApiError apiError, Exception exception, HttpServletRequest request);
}
