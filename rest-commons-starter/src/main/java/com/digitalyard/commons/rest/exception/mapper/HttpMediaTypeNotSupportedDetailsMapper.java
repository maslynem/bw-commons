package com.digitalyard.commons.rest.exception.mapper;


import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.util.Map;

public class HttpMediaTypeNotSupportedDetailsMapper extends ExceptionDetailsMapper<HttpMediaTypeNotSupportedException> {
    public static final String SUPPORTED = "SUPPORTED";
    public static final String CONTENT_TYPE = "CONTENT_TYPE";

    @Override
    public Map<String, Object> mapToDetails(HttpMediaTypeNotSupportedException ex) {
        return Map.of(
                CONTENT_TYPE, ex.getContentType() != null ? ex.getContentType().toString() : "unknown",
                SUPPORTED, ex.getSupportedMediaTypes().toString()
        );
    }

    @Override
    public Class<HttpMediaTypeNotSupportedException> canHandle() {
        return HttpMediaTypeNotSupportedException.class;
    }
}