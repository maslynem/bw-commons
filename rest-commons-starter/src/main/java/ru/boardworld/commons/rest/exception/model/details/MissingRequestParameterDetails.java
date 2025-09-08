package ru.boardworld.commons.rest.exception.model.details;

import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class MissingRequestParameterDetails extends ApiErrorDetails {
    private String parameterName;
    private String parameterType;
}