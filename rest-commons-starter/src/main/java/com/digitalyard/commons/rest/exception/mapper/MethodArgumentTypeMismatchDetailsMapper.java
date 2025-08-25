package com.digitalyard.commons.rest.exception.mapper;


import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

public class MethodArgumentTypeMismatchDetailsMapper extends ExceptionDetailsMapper<MethodArgumentTypeMismatchException> {
    public static final String PARAMETER_NAME = "PARAMETER_NAME";
    public static final String VALUE = "VALUE";
    public static final String REQUIRED_TYPE = "REQUIRED_TYPE";

    @Override
    public Map<String, Object> mapToDetails(MethodArgumentTypeMismatchException ex) {
        return Map.of(
                PARAMETER_NAME, ex.getName(),
                VALUE, ex.getValue() != null ? ex.getValue() : "unknown",
                REQUIRED_TYPE, ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
                );
    }

    @Override
    public Class<MethodArgumentTypeMismatchException> canHandle() {
        return MethodArgumentTypeMismatchException.class;
    }
}