package dy.digitalyard.commons.rest.exception.model.details;

import dy.digitalyard.commons.rest.exception.model.ApiErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@FieldNameConstants
@AllArgsConstructor
public class ArgumentTypeMismatchDetails extends ApiErrorDetails {
    private String parameterName;
    private Object value;
    private String requiredType;
}