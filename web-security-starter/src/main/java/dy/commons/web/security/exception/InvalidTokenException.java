package dy.commons.web.security.exception;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;

/**
 * Исключение для недействительных токенов.
 */
public class InvalidTokenException extends SecurityException {
    public static final String REASON = "reason";

    public InvalidTokenException(String reason) {
        super(CommonErrorCode.INVALID_TOKEN, Map.of(REASON, reason));
    }

}