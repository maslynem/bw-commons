package ru.boardworld.commons.rest.exception.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;
import ru.boardworld.commons.rest.jackson.ErrorCodeDeserializer;
import ru.boardworld.commons.rest.jackson.ErrorCodeSerializer;

@JsonSerialize(using = ErrorCodeSerializer.class)
@JsonDeserialize(using = ErrorCodeDeserializer.class)
public interface ErrorCode {
    String getCodeName();

    HttpStatus getHttpStatus();

    Class<? extends ApiErrorDetails> getDetailsClass();
}
