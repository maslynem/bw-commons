package com.digitalyard.commons.rest.exception.config;

import com.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import com.digitalyard.commons.rest.exception.handler.GlobalExceptionHandler;
import com.digitalyard.commons.rest.exception.mapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration
@Import(GlobalExceptionHandler.class)
public class ExceptionHandlerConfiguration {

    @Bean
    public ApiErrorFactory apiErrorFactory() {
        return new ApiErrorFactory();
    }

    @Bean
    public ExceptionDetailsMapperRegistry exceptionDetailsMapperRegistry(List<ExceptionDetailsMapper<? extends Exception>> mappers) {
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
