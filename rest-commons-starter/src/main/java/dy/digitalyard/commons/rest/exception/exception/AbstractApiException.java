package dy.digitalyard.commons.rest.exception.exception;

import dy.digitalyard.commons.rest.exception.model.ApiErrorDetails;
import dy.digitalyard.commons.rest.exception.model.ErrorCode;
import lombok.Getter;

@Getter
public abstract class AbstractApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final ApiErrorDetails details;

    protected AbstractApiException(ErrorCode errorCode, ApiErrorDetails details, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
        this.details = details;
    }

    protected AbstractApiException(ErrorCode errorCode, ApiErrorDetails details, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    protected AbstractApiException(ErrorCode errorCode, ApiErrorDetails details) {
        this(errorCode, details, null);
    }

}