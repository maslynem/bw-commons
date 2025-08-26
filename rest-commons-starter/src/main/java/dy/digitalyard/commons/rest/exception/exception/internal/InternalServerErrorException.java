package dy.digitalyard.commons.rest.exception.exception.internal;

import dy.digitalyard.commons.rest.exception.exception.AbstractApiException;
import dy.digitalyard.commons.rest.exception.model.ApiErrorDetails;
import dy.digitalyard.commons.rest.exception.model.CommonErrorCode;

public abstract class InternalServerErrorException extends AbstractApiException {

    protected InternalServerErrorException(CommonErrorCode commonErrorCode, ApiErrorDetails details) {
        super(commonErrorCode, details);
    }

    protected InternalServerErrorException(CommonErrorCode commonErrorCode, ApiErrorDetails details, String message) {
        super(commonErrorCode, details, message);
    }

}