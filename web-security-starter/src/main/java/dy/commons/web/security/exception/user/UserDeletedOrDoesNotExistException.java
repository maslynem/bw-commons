package dy.commons.web.security.exception.user;

import dy.commons.web.security.model.errorCode.SecurityErrorCode;


public class UserDeletedOrDoesNotExistException extends UserException {
    public UserDeletedOrDoesNotExistException(String login) {
        super(SecurityErrorCode.USER_DELETED_OR_DOES_NOT_EXIST, login);
    }
}