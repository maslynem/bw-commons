package ru.boardworld.commons.rest.openfeign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.boardworld.commons.rest.exception.model.ApiError;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
public class InternalFeignApiErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        try (InputStream bodyIs = response.body().asInputStream()) {
            ApiError apiError = objectMapper.readValue(bodyIs, ApiError.class);
            return new InternalFeignException(apiError);
        } catch (IOException e) {
            log.error("Failed to decode ApiError response. MethodKey: {}. Reason: {}", methodKey, e.getMessage());
            return defaultDecoder.decode(methodKey, response);
        }
    }
}