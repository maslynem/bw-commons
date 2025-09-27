package ru.boardworld.commons.rest.exception.model;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCodeName();

    HttpStatus getHttpStatus();

    Class<? extends ApiErrorDetails> getDetailsClass();
}
