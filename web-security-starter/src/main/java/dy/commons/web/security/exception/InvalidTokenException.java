package dy.commons.web.security.exception;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;

/**
 * Исключение для недействительных токенов.
 */
public class InvalidTokenException extends SecurityException {
    public static final String TOKEN = "token";

    public InvalidTokenException(String token) {
        super(CommonErrorCode.INVALID_TOKEN, Map.of(TOKEN, token));
    }

    public InvalidTokenException(String token, Throwable throwable) {
        super(CommonErrorCode.INVALID_TOKEN, Map.of(TOKEN, token), throwable);
    }

    public InvalidTokenException(Map<String, Object> details) {
        super(CommonErrorCode.INVALID_TOKEN, details);
    }

    public InvalidTokenException(Map<String, Object> details, Throwable throwable) {
        super(CommonErrorCode.INVALID_TOKEN, details, throwable);
    }
}