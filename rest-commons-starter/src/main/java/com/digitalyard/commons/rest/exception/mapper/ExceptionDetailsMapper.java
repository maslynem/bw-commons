package com.digitalyard.commons.rest.exception.mapper;

import java.util.Map;

public abstract class ExceptionDetailsMapper<T extends Exception> {
    public abstract Map<String, Object> mapToDetails(T exception);
    public abstract Class<T> canHandle();

    public boolean supports(Exception ex) {
        return canHandle().isInstance(ex);
    }
}
