package dy.digitalyard.commons.web.security.exception.user;

import dy.digitalyard.commons.web.security.model.errorCode.SecurityErrorCode;


public class UserDeletedOrDoesNotExistException extends UserException {
    public UserDeletedOrDoesNotExistException(String login) {
        super(SecurityErrorCode.USER_DELETED_OR_DOES_NOT_EXIST, login);
    }
}