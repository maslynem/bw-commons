package ru.boardworld.commons.rest.exception.mapper.impl;


import ru.boardworld.commons.rest.exception.mapper.ExceptionDetailsMapper;
import ru.boardworld.commons.rest.exception.model.details.ArgumentTypeMismatchDetails;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public class MethodArgumentTypeMismatchDetailsMapper extends ExceptionDetailsMapper<MethodArgumentTypeMismatchException, ArgumentTypeMismatchDetails> {
    @Override
    public ArgumentTypeMismatchDetails mapToDetails(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        Object value = ex.getValue() != null ? ex.getValue() : "unknown";
        String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
        return new ArgumentTypeMismatchDetails(parameterName, value, requiredType);
    }

    @Override
    public Class<MethodArgumentTypeMismatchException> canHandle() {
        return MethodArgumentTypeMismatchException.class;
    }
}