package com.digitalyard.commons.rest.exception.mapper;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConstraintViolationDetailsMapper extends ExceptionDetailsMapper<ConstraintViolationException> {
    public static final String VIOLATIONS = "VIOLATIONS";

    @Override
    public Map<String, Object> mapToDetails(ConstraintViolationException ex) {
        Map<String, List<String>> violations = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.groupingBy(
                        v -> {
                            String path = v.getPropertyPath().toString();
                            return path.substring(path.lastIndexOf('.') + 1);
                        },
                        LinkedHashMap::new,
                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                ));
        return Map.of(VIOLATIONS, violations);
    }

    @Override
    public Class<ConstraintViolationException> canHandle() {
        return ConstraintViolationException.class;
    }
}