package ru.boardworld.commons.web.security.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.rest.exception.model.ErrorCode;

@Getter
public abstract class SecurityException extends AuthenticationException {
    private final ErrorCode errorCode;
    private final ApiErrorDetails details;

    protected SecurityException(ErrorCode errorCode, ApiErrorDetails details) {
        super(errorCode.getCodeName());
        this.errorCode = errorCode;
        this.details = details;
    }
}
