package dy.digitalyard.commons.rest.exception.mapper.impl;

import dy.digitalyard.commons.rest.exception.mapper.ExceptionDetailsMapper;
import dy.digitalyard.commons.rest.exception.model.details.MissingRequestParameterDetails;
import org.springframework.web.bind.MissingServletRequestParameterException;

public class MissingRequestParameterDetailsMapper extends ExceptionDetailsMapper<MissingServletRequestParameterException, MissingRequestParameterDetails> {

    @Override
    public MissingRequestParameterDetails mapToDetails(MissingServletRequestParameterException ex) {
        return new MissingRequestParameterDetails(ex.getParameterName(), ex.getParameterType());
    }

    @Override
    public Class<MissingServletRequestParameterException> canHandle() {
        return MissingServletRequestParameterException.class;
    }
}