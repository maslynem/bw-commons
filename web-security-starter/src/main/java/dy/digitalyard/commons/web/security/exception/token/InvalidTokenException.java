package dy.digitalyard.commons.web.security.exception.token;

import dy.digitalyard.commons.web.security.exception.SecurityException;
import dy.digitalyard.commons.web.security.model.errorCode.SecurityErrorCode;
import dy.digitalyard.commons.web.security.model.errorCode.details.InvalidTokenDetails;


public class InvalidTokenException extends SecurityException {

    public InvalidTokenException(String reason) {
        super(SecurityErrorCode.INVALID_TOKEN, new InvalidTokenDetails(reason));
    }

}