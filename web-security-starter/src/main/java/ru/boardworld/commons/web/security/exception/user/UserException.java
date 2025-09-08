package ru.boardworld.commons.web.security.exception.user;

import ru.boardworld.commons.rest.exception.model.ErrorCode;
import ru.boardworld.commons.web.security.exception.SecurityException;
import ru.boardworld.commons.web.security.model.errorCode.details.UserErrorDetails;


public abstract class UserException extends SecurityException {

    public UserException(ErrorCode code, String login) {
        super(code, new UserErrorDetails(login));
    }

}