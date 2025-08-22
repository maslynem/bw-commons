package dy.commons.web.security.exception;

import com.digitalyard.commons.rest.exception.model.ErrorCode;
import com.digitalyard.commons.rest.exception.exception.AbstractApiException;

import java.util.Map;

/**
 * Базовый класс для всех исключений, связанных с безопасностью и аутентификацией.
 */
public abstract class SecurityException extends AbstractApiException {
    protected SecurityException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }

    protected SecurityException(ErrorCode errorCode, Map<String, Object> details, Throwable cause) {
        super(errorCode, details, cause);
    }
}