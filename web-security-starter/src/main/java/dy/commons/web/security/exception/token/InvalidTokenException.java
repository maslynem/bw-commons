package dy.commons.web.security.exception.token;

import dy.commons.web.security.exception.SecurityException;
import dy.commons.web.security.model.errorCode.SecurityErrorCode;

import java.util.Map;


public class InvalidTokenException extends SecurityException {
    public static final String REASON = "REASON";

    public InvalidTokenException(String reason) {
        super(SecurityErrorCode.INVALID_TOKEN, Map.of(REASON, reason));
    }

}