package ru.boardworld.commons.rest.exception.exception.db;

import ru.boardworld.commons.rest.exception.model.CommonErrorCode;
import ru.boardworld.commons.rest.exception.model.details.EntityNotFoundDetails;

import java.util.UUID;


public class EntityNotFoundException extends DataException {
    public EntityNotFoundException(String resourceType, UUID identifier) {
        super(CommonErrorCode.ENTITY_NOT_FOUND, new EntityNotFoundDetails(resourceType, identifier));
    }
}