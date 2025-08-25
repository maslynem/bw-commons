package dy.commons.web.security.exception.token;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;
import dy.commons.web.security.exception.SecurityException;

import java.time.Instant;
import java.util.Map;


public class TokenExpiredException extends SecurityException {
    public static final String EXPIRED_AT = "EXPIRED_AT";

    public TokenExpiredException(Instant expiredAt) {
        super(CommonErrorCode.TOKEN_EXPIRED,
                Map.of(EXPIRED_AT, expiredAt));
    }
}