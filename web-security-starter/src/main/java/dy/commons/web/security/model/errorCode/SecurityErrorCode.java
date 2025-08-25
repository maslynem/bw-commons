package dy.commons.web.security.model.errorCode;

import com.digitalyard.commons.rest.exception.model.ErrorCode;

public enum SecurityErrorCode implements ErrorCode {
    // Ошибки аутентификации/авторизации (4xx)
    UNAUTHORIZED,
    FORBIDDEN,
    INVALID_TOKEN,
    TOKEN_EXPIRED,
    BLOCKED_USER,
    USER_DELETED_OR_DOES_NOT_EXIST,
}
