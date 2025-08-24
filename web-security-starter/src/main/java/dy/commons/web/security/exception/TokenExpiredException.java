package dy.commons.web.security.exception;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.time.Instant;
import java.util.Map;


public class TokenExpiredException extends SecurityException {
    public static final String EXPIRED_AT = "expiredAt";

    public TokenExpiredException(Instant expiredAt) {
        super(CommonErrorCode.TOKEN_EXPIRED,
                Map.of(EXPIRED_AT, expiredAt));
    }
}