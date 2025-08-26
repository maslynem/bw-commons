package dy.digitalyard.commons.rest.exception.model.details;

import dy.digitalyard.commons.rest.exception.model.ApiErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ValidationFailedDetails extends ApiErrorDetails {
    private Map<String, List<String>> violations;
}