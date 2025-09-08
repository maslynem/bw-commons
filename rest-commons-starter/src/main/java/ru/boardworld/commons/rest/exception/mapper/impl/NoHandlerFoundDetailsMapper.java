package ru.boardworld.commons.rest.exception.mapper.impl;


import ru.boardworld.commons.rest.exception.mapper.ExceptionDetailsMapper;
import ru.boardworld.commons.rest.exception.model.details.NoHandlerFoundDetails;
import org.springframework.web.servlet.NoHandlerFoundException;

public class NoHandlerFoundDetailsMapper extends ExceptionDetailsMapper<NoHandlerFoundException, NoHandlerFoundDetails> {

    @Override
    public NoHandlerFoundDetails mapToDetails(NoHandlerFoundException ex) {
        return new NoHandlerFoundDetails(ex.getHttpMethod(), ex.getRequestURL());
    }

    @Override
    public Class<NoHandlerFoundException> canHandle() {
        return NoHandlerFoundException.class;
    }
}