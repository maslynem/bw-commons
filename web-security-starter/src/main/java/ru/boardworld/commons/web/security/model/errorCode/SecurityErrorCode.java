package ru.boardworld.commons.web.security.model.errorCode;

import org.springframework.http.HttpStatus;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.rest.exception.model.ErrorCode;
import ru.boardworld.commons.rest.exception.model.details.EmptyDetails;
import ru.boardworld.commons.web.security.model.errorCode.details.*;

public enum SecurityErrorCode implements ErrorCode {
    // Ошибки аутентификации/авторизации (4xx)
    UNAUTHORIZED(GenericUnauthorizedDetails.class, HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(InvalidTokenDetails.class, HttpStatus.UNAUTHORIZED),
    BAD_CREDENTIALS(EmptyDetails.class, HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(TokenExpiredDetails.class, HttpStatus.UNAUTHORIZED),
    USER_DELETED_OR_DOES_NOT_EXIST(UserErrorDetails.class, HttpStatus.UNAUTHORIZED),
    BLOCKED_USER(UserErrorDetails.class, HttpStatus.FORBIDDEN),
    FORBIDDEN(ForbiddenDetails.class, HttpStatus.FORBIDDEN),
    ;

    private final Class<? extends ApiErrorDetails> details;
    private final HttpStatus httpStatus;

    SecurityErrorCode(Class<? extends ApiErrorDetails> details, HttpStatus httpStatus) {
        this.details = details;
        this.httpStatus = httpStatus;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public Class<? extends ApiErrorDetails> getDetailsClass() {
        return details;
    }

    @Override
    public String getCodeName() {
        return this.name();
    }
}
