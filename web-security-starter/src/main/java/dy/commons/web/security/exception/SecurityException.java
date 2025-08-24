package dy.commons.web.security.exception;

import com.digitalyard.commons.rest.exception.model.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

@Getter
public abstract class SecurityException extends AuthenticationException {
    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    protected SecurityException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.name());
        this.errorCode = errorCode;
        this.details = details;
    }
}
