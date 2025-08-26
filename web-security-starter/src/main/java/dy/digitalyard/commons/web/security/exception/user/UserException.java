package dy.digitalyard.commons.web.security.exception.user;

import dy.digitalyard.commons.rest.exception.model.ErrorCode;
import dy.digitalyard.commons.web.security.exception.SecurityException;
import dy.digitalyard.commons.web.security.model.errorCode.details.UserErrorDetails;


public abstract class UserException extends SecurityException {

    public UserException(ErrorCode code, String login) {
        super(code, new UserErrorDetails(login));
    }

}