package dy.commons.web.security.exception;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;
import java.util.UUID;

/**
 * Исключение для заблокированных пользователей.
 */
public class BlockedUserException extends SecurityException {
    public static final String USER_ID = "userId";

    public BlockedUserException(UUID userId) {
        super(CommonErrorCode.BLOCKED_USER,
                Map.of(USER_ID, userId));
    }

    public BlockedUserException(Map<String, Object> details) {
        super(CommonErrorCode.BLOCKED_USER, details);
    }

    public BlockedUserException(Map<String, Object> details, Throwable throwable) {
        super(CommonErrorCode.BLOCKED_USER, details, throwable);
    }
}