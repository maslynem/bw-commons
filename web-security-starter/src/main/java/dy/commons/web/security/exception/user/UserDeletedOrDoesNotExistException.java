package dy.commons.web.security.exception.user;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;


public class UserDeletedOrDoesNotExistException extends UserException {
    public UserDeletedOrDoesNotExistException(String login) {
        super(CommonErrorCode.USER_DELETED_OR_DOES_NOT_EXIST, login);
    }
}