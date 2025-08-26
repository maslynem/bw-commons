package dy.digitalyard.commons.rest.exception.mapper;

import dy.digitalyard.commons.rest.exception.model.ApiErrorDetails;

public abstract class ExceptionDetailsMapper<T extends Exception, D extends ApiErrorDetails> {

    public abstract D mapToDetails(T exception);

    public abstract Class<T> canHandle();

    public boolean supports(Exception ex) {
        return canHandle().isInstance(ex);
    }
}
