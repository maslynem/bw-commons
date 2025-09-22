package ru.boardworld.commons.rest.exception.exception.db;

import ru.boardworld.commons.rest.exception.model.CommonErrorCode;
import ru.boardworld.commons.rest.exception.model.details.EntityNotFoundDetails;


public class EntityNotFoundException extends DataException {
    public EntityNotFoundException(String resourceType, String value) {
        super(CommonErrorCode.ENTITY_NOT_FOUND, new EntityNotFoundDetails(resourceType, value));
    }
}