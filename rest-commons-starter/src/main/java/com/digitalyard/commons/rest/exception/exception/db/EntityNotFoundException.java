package com.digitalyard.commons.rest.exception.exception.db;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;
import java.util.UUID;


public class EntityNotFoundException extends DataException {
    public static final String RESOURCE_TYPE = "RESOURCE_TYPE";
    public static final String ID = "ID";

    public EntityNotFoundException(String resourceType, UUID identifier) {
        super(CommonErrorCode.ENTITY_NOT_FOUND,
                Map.of(RESOURCE_TYPE, resourceType, ID, identifier));
    }

}