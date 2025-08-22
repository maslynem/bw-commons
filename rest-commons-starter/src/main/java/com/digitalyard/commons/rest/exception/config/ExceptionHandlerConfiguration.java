package com.digitalyard.commons.rest.exception.config;

import com.digitalyard.commons.rest.exception.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(GlobalExceptionHandler.class)
@ConditionalOnMissingBean(GlobalExceptionHandler.class)
public class ExceptionHandlerConfiguration {
}
