package ru.boardworld.commons.web.security.exception;

import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.rest.exception.model.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public abstract class SecurityException extends AuthenticationException {
    private final ErrorCode errorCode;
    private final ApiErrorDetails details;

    protected SecurityException(ErrorCode errorCode, ApiErrorDetails details) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.details = details;
    }
}
