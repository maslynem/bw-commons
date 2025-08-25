package dy.commons.web.security.exception.user;

import com.digitalyard.commons.rest.exception.model.ErrorCode;
import dy.commons.web.security.exception.SecurityException;

import java.util.Map;


public abstract class UserException extends SecurityException {
    public static final String USER_LOGIN = "USER_LOGIN";

    public UserException(ErrorCode code, String login) {
        super(code, Map.of(USER_LOGIN, login));
    }

}