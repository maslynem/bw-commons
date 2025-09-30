package ru.boardworld.commons.rest.jackson;

import org.springframework.http.HttpStatus;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.rest.exception.model.ErrorCode;

/**
 * A concrete implementation of ErrorCode used for deserialization
 * on the client side (e.g., in an OpenFeign decoder).
 */
public class TransportableErrorCode implements ErrorCode {

    private final String codeName;
    private final HttpStatus httpStatus;

    public TransportableErrorCode(String codeName, HttpStatus httpStatus) {
        this.codeName = codeName;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCodeName() {
        return codeName;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public Class<? extends ApiErrorDetails> getDetailsClass() {
        return null;
    }
}