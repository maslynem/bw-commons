package ru.boardworld.commons.web.security.exception.token;

import ru.boardworld.commons.web.security.exception.SecurityException;
import ru.boardworld.commons.web.security.model.errorCode.SecurityErrorCode;
import ru.boardworld.commons.web.security.model.errorCode.details.TokenExpiredDetails;

import java.time.Instant;


public class TokenExpiredException extends SecurityException {
    public TokenExpiredException(Instant expiredAt) {
        super(SecurityErrorCode.TOKEN_EXPIRED,
                new TokenExpiredDetails(expiredAt));
    }
}