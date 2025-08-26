package dy.digitalyard.commons.rest.exception.mapper.impl;


import dy.digitalyard.commons.rest.exception.mapper.ExceptionDetailsMapper;
import dy.digitalyard.commons.rest.exception.model.details.MethodNotSupportedDetails;
import org.springframework.http.HttpMethod;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class HttpRequestMethodNotSupportedDetailsMapper extends ExceptionDetailsMapper<HttpRequestMethodNotSupportedException, MethodNotSupportedDetails> {

    @Override
    public MethodNotSupportedDetails mapToDetails(HttpRequestMethodNotSupportedException ex) {
        Set<HttpMethod> httpMethods = Optional.ofNullable(ex.getSupportedHttpMethods()).orElseGet(Collections::emptySet);
        List<String> supportedMethods = httpMethods.stream().map(HttpMethod::name).toList();
        return new MethodNotSupportedDetails(supportedMethods);
    }

    @Override
    public Class<HttpRequestMethodNotSupportedException> canHandle() {
        return HttpRequestMethodNotSupportedException.class;
    }
}