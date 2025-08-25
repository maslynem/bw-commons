package com.digitalyard.commons.rest.exception.mapper;


import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;

public class NoHandlerFoundDetailsMapper extends ExceptionDetailsMapper<NoHandlerFoundException> {
    public static final String METHOD = "METHOD";
    public static final String URL = "URL";

    @Override
    public Map<String, Object> mapToDetails(NoHandlerFoundException ex) {
        return Map.of(METHOD, ex.getHttpMethod(), URL, ex.getRequestURL());
    }

    @Override
    public Class<NoHandlerFoundException> canHandle() {
        return NoHandlerFoundException.class;
    }
}