package ru.boardworld.commons.rest.jackson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;

import java.util.Map;

/**
 * A generic, map-based implementation of ApiErrorDetails used for deserializing
 * unknown 'details' objects on the client side without losing information.
 */
public class GenericApiErrorDetails extends ApiErrorDetails {

    private final Map<String, Object> properties;

    public GenericApiErrorDetails(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * This annotation tells Jackson to "unwrap" this map during serialization,
     * turning its key-value pairs into top-level properties of the JSON object.
     */
    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }
}