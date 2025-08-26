package dy.digitalyard.commons.rest.exception.mapper.impl;


import dy.digitalyard.commons.rest.exception.mapper.ExceptionDetailsMapper;
import dy.digitalyard.commons.rest.exception.model.details.UnsupportedMediaTypeDetails;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import java.util.List;

public class HttpMediaTypeNotSupportedDetailsMapper extends ExceptionDetailsMapper<HttpMediaTypeNotSupportedException, UnsupportedMediaTypeDetails> {

    @Override
    public UnsupportedMediaTypeDetails mapToDetails(HttpMediaTypeNotSupportedException ex) {
        String contentType = ex.getContentType() != null ? ex.getContentType().toString() : "unknown";
        List<String> supported = ex.getSupportedMediaTypes().stream().map(MediaType::toString).toList();
        return new UnsupportedMediaTypeDetails(contentType, supported);
    }

    @Override
    public Class<HttpMediaTypeNotSupportedException> canHandle() {
        return HttpMediaTypeNotSupportedException.class;
    }
}