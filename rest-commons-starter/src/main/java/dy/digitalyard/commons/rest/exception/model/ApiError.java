package dy.digitalyard.commons.rest.exception.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@FieldNameConstants
public class ApiError {
    private UUID id;
    private ErrorCode code;
    private int status;
    private Instant timestamp;
    private ApiErrorDetails details;
}