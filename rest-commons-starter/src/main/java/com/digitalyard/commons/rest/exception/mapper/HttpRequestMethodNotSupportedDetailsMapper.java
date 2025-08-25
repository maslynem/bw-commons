package com.digitalyard.commons.rest.exception.mapper;


import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Map;

public class HttpRequestMethodNotSupportedDetailsMapper extends ExceptionDetailsMapper<HttpRequestMethodNotSupportedException> {
    public static final String SUPPORTED_METHODS = "SUPPORTED_METHODS";

    @Override
    public Map<String, Object> mapToDetails(HttpRequestMethodNotSupportedException ex) {
        return Map.of(
                SUPPORTED_METHODS, ex.getSupportedHttpMethods() != null ? ex.getSupportedHttpMethods().toString() : "[]"
        );
    }

    @Override
    public Class<HttpRequestMethodNotSupportedException> canHandle() {
        return HttpRequestMethodNotSupportedException.class;
    }
}