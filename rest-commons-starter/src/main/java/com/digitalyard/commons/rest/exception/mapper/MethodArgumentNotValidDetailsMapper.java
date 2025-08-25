package com.digitalyard.commons.rest.exception.mapper;


import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MethodArgumentNotValidDetailsMapper extends ExceptionDetailsMapper<MethodArgumentNotValidException> {
    public static final String VIOLATIONS = "VIOLATIONS";

    @Override
    public Map<String, Object> mapToDetails(MethodArgumentNotValidException ex) {
        Map<String, List<String>> grouped = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(err -> grouped
                        .computeIfAbsent(err.getField(), key -> new ArrayList<>())
                        .add(err.getDefaultMessage() != null ? err.getDefaultMessage() : "Invalid value"));

        List<String> global = ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(e -> e.getDefaultMessage() != null ? e.getDefaultMessage() : "Validation error")
                .collect(Collectors.toList());
        if (!global.isEmpty()) {
            grouped.put("_global", global);
        }
        return Map.of(VIOLATIONS, grouped);
    }

    @Override
    public Class<MethodArgumentNotValidException> canHandle() {
        return MethodArgumentNotValidException.class;
    }
}