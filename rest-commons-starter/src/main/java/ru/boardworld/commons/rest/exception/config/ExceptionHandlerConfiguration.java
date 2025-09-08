package ru.boardworld.commons.rest.exception.config;

import ru.boardworld.commons.rest.exception.handler.ApiErrorFactory;
import ru.boardworld.commons.rest.exception.handler.GlobalExceptionHandler;
import ru.boardworld.commons.rest.exception.mapper.ExceptionDetailsMapper;
import ru.boardworld.commons.rest.exception.mapper.ExceptionDetailsMapperRegistry;
import ru.boardworld.commons.rest.exception.mapper.impl.*;
import ru.boardworld.commons.rest.exception.mapper.impl.MethodArgumentNotValidDetailsMapper;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.utils.yaml.CustomConfigPropertiesReaderFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@AutoConfiguration
@Import(GlobalExceptionHandler.class)
@PropertySource(value = "classpath:web-settings.yaml", factory = CustomConfigPropertiesReaderFactory.class)
public class ExceptionHandlerConfiguration {

    @Bean
    public ApiErrorFactory apiErrorFactory() {
        return new ApiErrorFactory();
    }

    @Bean
    public ExceptionDetailsMapperRegistry exceptionDetailsMapperRegistry(List<ExceptionDetailsMapper<? extends Exception, ? extends ApiErrorDetails>> mappers) {
        return new ExceptionDetailsMapperRegistry(mappers);
    }

    @Bean
    public MethodArgumentNotValidDetailsMapper methodArgumentNotValidDetailsMapper() {
        return new MethodArgumentNotValidDetailsMapper();
    }

    @Bean
    public ConstraintViolationDetailsMapper constraintViolationDetailsMapper() {
        return new ConstraintViolationDetailsMapper();
    }

    @Bean
    public NoHandlerFoundDetailsMapper noHandlerFoundDetailsMapper() {
        return new NoHandlerFoundDetailsMapper();
    }

    @Bean
    public MissingRequestParameterDetailsMapper missingRequestParameterDetailsMapper() {
        return new MissingRequestParameterDetailsMapper();
    }

    @Bean
    public MethodArgumentTypeMismatchDetailsMapper methodArgumentTypeMismatchDetailsMapper() {
        return new MethodArgumentTypeMismatchDetailsMapper();
    }

    @Bean
    public HttpMessageNotReadableDetailsMapper httpMessageNotReadableDetailsMapper() {
        return new HttpMessageNotReadableDetailsMapper();
    }

    @Bean
    public HttpMediaTypeNotSupportedDetailsMapper httpMediaTypeNotSupportedDetailsMapper() {
        return new HttpMediaTypeNotSupportedDetailsMapper();
    }

    @Bean
    public HttpRequestMethodNotSupportedDetailsMapper httpRequestMethodNotSupportedDetailsMapper() {
        return new HttpRequestMethodNotSupportedDetailsMapper();
    }
}
