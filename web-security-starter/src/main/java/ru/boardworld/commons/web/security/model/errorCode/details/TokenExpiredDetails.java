package ru.boardworld.commons.web.security.model.errorCode.details;

import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@FieldNameConstants
@AllArgsConstructor
public class TokenExpiredDetails extends ApiErrorDetails {
    private Instant expiredAt;
}
