package dy.commons.web.security.exception.user;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;


public class BlockedUserException extends UserException {
    public BlockedUserException(String login) {
        super(CommonErrorCode.BLOCKED_USER, login);
    }
}