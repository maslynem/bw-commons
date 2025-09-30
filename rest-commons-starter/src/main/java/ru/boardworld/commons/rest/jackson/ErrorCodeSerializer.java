package ru.boardworld.commons.rest.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ru.boardworld.commons.rest.exception.model.ErrorCode;

import java.io.IOException;

public class ErrorCodeSerializer extends JsonSerializer<ErrorCode> {
    @Override
    public void serialize(ErrorCode value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("codeName", value.getCodeName());
        gen.writeNumberField("httpStatus", value.getHttpStatus().value());
        gen.writeEndObject();
    }
}