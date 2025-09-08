package ru.boardworld.commons.rest.exception.model.details;

import com.fasterxml.jackson.annotation.JsonValue;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;

import java.util.Collections;
import java.util.Map;


public final class EmptyDetails extends ApiErrorDetails {
    public static final EmptyDetails INSTANCE = new EmptyDetails();
    private EmptyDetails() {}

    @JsonValue
    public Map<String, Object> json() {
        return Collections.emptyMap();
    }
}
