package com.digitalyard.commons.rest.exception.mapper;

import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.Map;

public class MissingRequestParameterDetailsMapper extends ExceptionDetailsMapper<MissingServletRequestParameterException> {
    public static final String PARAMETER_NAME = "PARAMETER_NAME";
    public static final String PARAMETER_TYPE = "PARAMETER_TYPE";

    @Override
    public Map<String, Object> mapToDetails(MissingServletRequestParameterException ex) {
        return Map.of(PARAMETER_NAME, ex.getParameterName(), PARAMETER_TYPE, ex.getParameterType());
    }

    @Override
    public Class<MissingServletRequestParameterException> canHandle() {
        return MissingServletRequestParameterException.class;
    }
}