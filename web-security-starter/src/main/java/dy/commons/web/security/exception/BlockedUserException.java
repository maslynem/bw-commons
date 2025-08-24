package dy.commons.web.security.exception;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;


public class BlockedUserException extends SecurityException {
    public static final String USER_LOGIN = "userLogin";

    public BlockedUserException(String login) {
        super(CommonErrorCode.BLOCKED_USER,
                Map.of(USER_LOGIN, login));
    }

}