package ru.boardworld.commons.rest.exception.exception.internal;

import ru.boardworld.commons.rest.exception.model.CommonErrorCode;
import ru.boardworld.commons.rest.exception.model.details.ServiceUnavailableDetails;


public class InternalServerUnavailableException extends InternalServerErrorException {
    public InternalServerUnavailableException(String serviceName) {
        super(CommonErrorCode.SERVICE_UNAVAILABLE, new ServiceUnavailableDetails(serviceName));
    }
}