package ru.boardworld.commons.rest.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import ru.boardworld.commons.rest.exception.model.ErrorCode;

import java.io.IOException;

public class ErrorCodeDeserializer extends JsonDeserializer<ErrorCode> {
    @Override
    public ErrorCode deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String codeName = node.get("codeName").asText();
        int status = node.get("httpStatus").asInt();

        return new TransportableErrorCode(codeName, HttpStatus.valueOf(status));
    }
}