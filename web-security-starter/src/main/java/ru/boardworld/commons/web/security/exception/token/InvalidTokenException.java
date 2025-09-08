package ru.boardworld.commons.web.security.exception.token;

import ru.boardworld.commons.web.security.exception.SecurityException;
import ru.boardworld.commons.web.security.model.errorCode.SecurityErrorCode;
import ru.boardworld.commons.web.security.model.errorCode.details.InvalidTokenDetails;


public class InvalidTokenException extends SecurityException {

    public InvalidTokenException(String reason) {
        super(SecurityErrorCode.INVALID_TOKEN, new InvalidTokenDetails(reason));
    }

}