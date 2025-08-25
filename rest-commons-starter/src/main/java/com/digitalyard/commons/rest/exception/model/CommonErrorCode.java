package com.digitalyard.commons.rest.exception.model;

public enum CommonErrorCode implements ErrorCode {

    // Ошибки валидации (4xx)
    HTTP_MESSAGE_NOT_READABLE,
    VALIDATION_FAILED,
    REQUIRED_PARAMETER_MISSING,
    INVALID_PARAMETER_VALUE,
    METHOD_NOT_ALLOWED,
    UNSUPPORTED_MEDIA_TYPE,

    // (400)
    ENTITY_NOT_FOUND,

    // (404)
    NOT_FOUND,

    // Ошибки на стороне сервера (5xx)
    UNKNOWN_INTERNAL_ERROR,
    SERVICE_UNAVAILABLE,
    DATABASE_ERROR,


}