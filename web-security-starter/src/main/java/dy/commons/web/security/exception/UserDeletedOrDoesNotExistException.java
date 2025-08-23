package dy.commons.web.security.exception;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;

/**
 * Исключение для удаленных или несуществующих пользователей.
 */
public class UserDeletedOrDoesNotExistException extends SecurityException {
    public static final String USER_LOGIN = "userLogin";

    public UserDeletedOrDoesNotExistException(String login) {
        super(CommonErrorCode.USER_DELETED_OR_DOES_NOT_EXIST,
                Map.of(USER_LOGIN, login));
    }

}