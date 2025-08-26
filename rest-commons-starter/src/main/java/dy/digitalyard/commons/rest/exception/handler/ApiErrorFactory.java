package dy.digitalyard.commons.rest.exception.handler;

import dy.digitalyard.commons.rest.exception.model.ApiError;
import dy.digitalyard.commons.rest.exception.model.ApiErrorDetails;
import dy.digitalyard.commons.rest.exception.model.ErrorCode;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class ApiErrorFactory {

    public ApiError create(ErrorCode errorCode, ApiErrorDetails details) {
        return ApiError.builder()
                .id(UUID.randomUUID())
                .code(errorCode)
                .status(errorCode.getHttpStatus().value())
                .timestamp(Instant.now())
                .details(details)
                .build();
    }

}