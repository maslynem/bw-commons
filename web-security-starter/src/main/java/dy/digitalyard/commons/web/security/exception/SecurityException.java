package dy.digitalyard.commons.web.security.exception;

import dy.digitalyard.commons.rest.exception.model.ApiErrorDetails;
import dy.digitalyard.commons.rest.exception.model.ErrorCode;
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
