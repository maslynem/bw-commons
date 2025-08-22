package com.digitalyard.commons.rest.exception.model;

public enum CommonErrorCode implements ErrorCode {

    // Ошибки валидации (4xx)
    VALIDATION_FAILED,
    REQUIRED_PARAMETER_MISSING,
    INVALID_PARAMETER_VALUE,

    // (404)
    NOT_FOUND,

    // Ошибки аутентификации/авторизации (4xx)
    UNAUTHORIZED,
    FORBIDDEN,
    INVALID_TOKEN,
    TOKEN_EXPIRED,
    BLOCKED_USER,
    USER_DELETED_OR_DOES_NOT_EXIST,

    // Ошибки на стороне сервера (5xx)
    INTERNAL_ERROR,
    SERVICE_UNAVAILABLE,
    DATABASE_ERROR,

    // Ошибки интеграции с внешними сервисами
    EXTERNAL_SERVICE_ERROR,
    EXTERNAL_SERVICE_TIMEOUT;

}