package dy.commons.web.security.exception;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.time.Instant;
import java.util.Map;

/**
 * Исключение для просроченных токенов.
 */
public class TokenExpiredException extends SecurityException {
    public static final String TOKEN = "token";
    public static final String EXPIRED_AT = "expiredAt";

    public TokenExpiredException(String token, Instant expiredAt) {
        super(CommonErrorCode.TOKEN_EXPIRED,
                Map.of(TOKEN, token, EXPIRED_AT, expiredAt));
    }

    public TokenExpiredException(Map<String, Object> details) {
        super(CommonErrorCode.TOKEN_EXPIRED, details);
    }
    public TokenExpiredException(Map<String, Object> details, Throwable throwable) {
        super(CommonErrorCode.TOKEN_EXPIRED, details, throwable);
    }
}