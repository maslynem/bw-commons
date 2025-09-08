package ru.boardworld.commons.rest.exception.mapper.impl;


import ru.boardworld.commons.rest.exception.mapper.ExceptionDetailsMapper;
import ru.boardworld.commons.rest.exception.model.details.HttpMessageNotReadableDetails;
import org.springframework.http.converter.HttpMessageNotReadableException;


public class HttpMessageNotReadableDetailsMapper extends ExceptionDetailsMapper<HttpMessageNotReadableException, HttpMessageNotReadableDetails> {
    @Override
    public HttpMessageNotReadableDetails mapToDetails(HttpMessageNotReadableException ex) {
        return new HttpMessageNotReadableDetails(ex.getMessage());
    }

    @Override
    public Class<HttpMessageNotReadableException> canHandle() {
        return HttpMessageNotReadableException.class;
    }
}