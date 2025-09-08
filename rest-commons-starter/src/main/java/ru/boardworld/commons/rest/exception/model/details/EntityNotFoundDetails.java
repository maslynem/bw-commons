package ru.boardworld.commons.rest.exception.model.details;

import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class EntityNotFoundDetails extends ApiErrorDetails {
    private String resourceType;
    private UUID id;
}