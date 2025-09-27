package ru.boardworld.commons.rest.exception.model;

import org.springframework.http.HttpStatus;
import ru.boardworld.commons.rest.exception.model.details.*;

public enum CommonErrorCode implements ErrorCode {

    // Ошибки валидации (4xx)
    HTTP_MESSAGE_NOT_READABLE(HttpMessageNotReadableDetails.class, HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED(ValidationFailedDetails.class, HttpStatus.BAD_REQUEST),
    REQUIRED_PARAMETER_MISSING(MissingRequestParameterDetails.class, HttpStatus.BAD_REQUEST),
    ARGUMENT_TYPE_MISMATCH(ArgumentTypeMismatchDetails.class, HttpStatus.BAD_REQUEST),
    METHOD_NOT_SUPPORTED(MethodNotSupportedDetails.class, HttpStatus.METHOD_NOT_ALLOWED),
    UNSUPPORTED_MEDIA_TYPE(UnsupportedMediaTypeDetails.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE),

    // (400)
    ENTITY_NOT_FOUND(EntityNotFoundDetails.class, HttpStatus.NOT_FOUND),

    // (404)
    NOT_FOUND(NoHandlerFoundDetails.class, HttpStatus.NOT_FOUND),

    // Ошибки на стороне сервера (5xx)
    UNKNOWN_INTERNAL_ERROR(UnknownInternalErrorDetails.class, HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE(ServiceUnavailableDetails.class, HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final Class<? extends ApiErrorDetails> detailsClass;
    private final HttpStatus defaultHttpStatus;

    CommonErrorCode(Class<? extends ApiErrorDetails> detailsClass, HttpStatus defaultHttpStatus) {
        this.detailsClass = detailsClass;
        this.defaultHttpStatus = defaultHttpStatus;
    }

    @Override
    public String getCodeName() {
        return this.name();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return defaultHttpStatus;
    }

    @Override
    public Class<? extends ApiErrorDetails> getDetailsClass() {
        return detailsClass;
    }
}