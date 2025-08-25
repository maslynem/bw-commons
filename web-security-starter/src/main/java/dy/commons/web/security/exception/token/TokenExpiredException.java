package dy.commons.web.security.exception.token;

import dy.commons.web.security.exception.SecurityException;
import dy.commons.web.security.model.errorCode.SecurityErrorCode;

import java.time.Instant;
import java.util.Map;


public class TokenExpiredException extends SecurityException {
    public static final String EXPIRED_AT = "EXPIRED_AT";

    public TokenExpiredException(Instant expiredAt) {
        super(SecurityErrorCode.TOKEN_EXPIRED,
                Map.of(EXPIRED_AT, expiredAt));
    }
}