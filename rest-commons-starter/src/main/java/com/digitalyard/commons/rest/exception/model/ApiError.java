package com.digitalyard.commons.rest.exception.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@FieldNameConstants
public class ApiError {
    private UUID id;
    private int status;
    private ErrorCode code;
    private Instant timestamp;
    private Map<String, Object> details;
}