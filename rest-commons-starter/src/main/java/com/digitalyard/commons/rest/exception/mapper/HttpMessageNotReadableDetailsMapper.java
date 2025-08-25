package com.digitalyard.commons.rest.exception.mapper;


import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Map;

public class HttpMessageNotReadableDetailsMapper extends ExceptionDetailsMapper<HttpMessageNotReadableException> {
    public static final String REASON = "REASON";

    @Override
    public Map<String, Object> mapToDetails(HttpMessageNotReadableException ex) {
        return Map.of(REASON, ex.getMessage());
    }

    @Override
    public Class<HttpMessageNotReadableException> canHandle() {
        return HttpMessageNotReadableException.class;
    }
}