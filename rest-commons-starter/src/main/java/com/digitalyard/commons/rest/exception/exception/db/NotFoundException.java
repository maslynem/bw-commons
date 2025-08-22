package com.digitalyard.commons.rest.exception.exception.db;

import com.digitalyard.commons.rest.exception.model.CommonErrorCode;

import java.util.Map;
import java.util.UUID;

/**
 * Исключение для случаев, когда запрашиваемый ресурс не найден.
 */
public class NotFoundException extends DataException {
    public static final String RESOURCE_TYPE = "resourceType";
    public static final String ID = "id";

    public NotFoundException(String resourceType, UUID identifier) {
        super(CommonErrorCode.NOT_FOUND,
                Map.of(RESOURCE_TYPE, resourceType, ID, identifier));
    }

}