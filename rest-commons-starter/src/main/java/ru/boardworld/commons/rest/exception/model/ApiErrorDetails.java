package ru.boardworld.commons.rest.exception.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ru.boardworld.commons.rest.jackson.ApiErrorDetailsDeserializer;

@JsonDeserialize(using = ApiErrorDetailsDeserializer.class)
public abstract class ApiErrorDetails {
}
