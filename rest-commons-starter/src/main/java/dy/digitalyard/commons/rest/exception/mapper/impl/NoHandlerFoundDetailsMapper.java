package dy.digitalyard.commons.rest.exception.mapper.impl;


import dy.digitalyard.commons.rest.exception.mapper.ExceptionDetailsMapper;
import dy.digitalyard.commons.rest.exception.model.details.NoHandlerFoundDetails;
import org.springframework.web.servlet.NoHandlerFoundException;

public class NoHandlerFoundDetailsMapper extends ExceptionDetailsMapper<NoHandlerFoundException, NoHandlerFoundDetails> {

    @Override
    public NoHandlerFoundDetails mapToDetails(NoHandlerFoundException ex) {
        return new NoHandlerFoundDetails(ex.getHttpMethod(), ex.getRequestURL());
    }

    @Override
    public Class<NoHandlerFoundException> canHandle() {
        return NoHandlerFoundException.class;
    }
}