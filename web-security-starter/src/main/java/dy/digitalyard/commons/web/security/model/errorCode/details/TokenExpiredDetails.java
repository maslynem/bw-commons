package dy.digitalyard.commons.web.security.model.errorCode.details;

import dy.digitalyard.commons.rest.exception.model.ApiErrorDetails;
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
