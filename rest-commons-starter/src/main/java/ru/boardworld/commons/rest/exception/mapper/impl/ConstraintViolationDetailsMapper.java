package ru.boardworld.commons.rest.exception.mapper.impl;


import ru.boardworld.commons.rest.exception.mapper.ExceptionDetailsMapper;
import ru.boardworld.commons.rest.exception.model.details.ValidationFailedDetails;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConstraintViolationDetailsMapper extends ExceptionDetailsMapper<ConstraintViolationException, ValidationFailedDetails> {

    @Override
    public ValidationFailedDetails mapToDetails(ConstraintViolationException ex) {
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
        return new ValidationFailedDetails(violations);
    }

    @Override
    public Class<ConstraintViolationException> canHandle() {
        return ConstraintViolationException.class;
    }
}