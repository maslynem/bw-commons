package dy.digitalyard.commons.web.security.exception.token;

import dy.digitalyard.commons.web.security.exception.SecurityException;
import dy.digitalyard.commons.web.security.model.errorCode.SecurityErrorCode;
import dy.digitalyard.commons.web.security.model.errorCode.details.TokenExpiredDetails;

import java.time.Instant;


public class TokenExpiredException extends SecurityException {
    public TokenExpiredException(Instant expiredAt) {
        super(SecurityErrorCode.TOKEN_EXPIRED,
                new TokenExpiredDetails(expiredAt));
    }
}