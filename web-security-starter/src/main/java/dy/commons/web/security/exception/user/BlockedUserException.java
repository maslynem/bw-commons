package dy.commons.web.security.exception.user;

import dy.commons.web.security.model.errorCode.SecurityErrorCode;


public class BlockedUserException extends UserException {
    public BlockedUserException(String login) {
        super(SecurityErrorCode.BLOCKED_USER, login);
    }
}