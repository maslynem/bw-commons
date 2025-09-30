package ru.boardworld.commons.rest.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.rest.exception.model.details.EmptyDetails;

import java.io.IOException;
import java.util.Map;

public class ApiErrorDetailsDeserializer extends JsonDeserializer<ApiErrorDetails> {

    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

    @Override
    public ApiErrorDetails deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Map<String, Object> properties = p.readValueAs(MAP_TYPE_REFERENCE);

        if (properties == null || properties.isEmpty()) {
            return EmptyDetails.INSTANCE;
        }

        return new GenericApiErrorDetails(properties);
    }
}