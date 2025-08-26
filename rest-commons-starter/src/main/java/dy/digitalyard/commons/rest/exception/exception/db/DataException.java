package dy.digitalyard.commons.rest.exception.exception.db;

import dy.digitalyard.commons.rest.exception.exception.AbstractApiException;
import dy.digitalyard.commons.rest.exception.model.ApiErrorDetails;
import dy.digitalyard.commons.rest.exception.model.ErrorCode;


public abstract class DataException extends AbstractApiException {
    protected DataException(ErrorCode errorCode, ApiErrorDetails details) {
        super(errorCode, details);
    }

    protected DataException(ErrorCode errorCode, ApiErrorDetails details, String message) {
        super(errorCode, details, message);
    }
}