package dy.commons.web.security.exception.token;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;
import dy.commons.web.security.exception.SecurityException;

import java.util.Map;


public class InvalidTokenException extends SecurityException {
    public static final String REASON = "REASON";

    public InvalidTokenException(String reason) {
        super(CommonErrorCode.INVALID_TOKEN, Map.of(REASON, reason));
    }

}