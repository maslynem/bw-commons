package ru.boardworld.commons.rest.openfeign;

import lombok.Getter;
import ru.boardworld.commons.rest.exception.model.ApiError;

@Getter
public class InternalFeignException extends RuntimeException {
    private final ApiError apiError;

    public InternalFeignException(ApiError apiError) {
        super("API error: " + apiError.getCode().getCodeName());
        this.apiError = apiError;
    }
}
