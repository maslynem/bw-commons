package ru.boardworld.commons.rest.exception.exception.internal;

import ru.boardworld.commons.rest.exception.exception.AbstractApiException;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.rest.exception.model.CommonErrorCode;

public abstract class InternalServerErrorException extends AbstractApiException {

    protected InternalServerErrorException(CommonErrorCode commonErrorCode, ApiErrorDetails details) {
        super(commonErrorCode, details);
    }

    protected InternalServerErrorException(CommonErrorCode commonErrorCode, ApiErrorDetails details, String message) {
        super(commonErrorCode, details, message);
    }

}