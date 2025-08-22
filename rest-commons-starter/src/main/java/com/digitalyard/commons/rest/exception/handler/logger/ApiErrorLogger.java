package com.digitalyard.commons.rest.exception.handler.logger;

import com.digitalyard.commons.rest.exception.model.ApiError;

public interface ApiErrorLogger {
    void logError(ApiError apiError);
}
