package com.digitalyard.commons.rest.exception.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import com.digitalyard.commons.rest.exception.handler.logger.ApiErrorLogger;
import com.digitalyard.commons.rest.exception.handler.logger.Slf4jApiErrorLogger;
import com.digitalyard.commons.rest.exception.resolver.ErrorMessageResolver;
import com.digitalyard.commons.rest.exception.resolver.SpElErrorMessageResolver;
import com.digitalyard.commons.rest.exception.resolver.basename.scanner.MessageBasenameScanner;
import com.digitalyard.commons.rest.exception.resolver.basename.scanner.impl.ClasspathMessageBasenameScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;

@Configuration
@EnableConfigurationProperties(ErrorMessageProperties.class)
@Slf4j
public class ErrorMessageConfiguration {

    @Bean
    public ExpressionParser parser() {
        return new SpelExpressionParser();
    }

    @Bean
    public ParserContext parserContext() {
        return new TemplateParserContext();
    }

    @Bean
    public MessageBasenameScanner messageBasenameScanner() {
        return new ClasspathMessageBasenameScanner();
    }

    @Bean
    public MessageSource errorMessageSource(MessageBasenameScanner basenameScanner,
                                            ErrorMessageProperties properties) {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        List<String> basenames = basenameScanner.scanBasenames();
        messageSource.setBasenames(basenames.toArray(new String[0]));

        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(false);

        messageSource.setCacheMillis(properties.getCacheTimeout().toMillis());

        return messageSource;
    }

    @Bean
    public ErrorMessageResolver errorMessageResolver(MessageSource errorMessageSource,
                                                     ErrorMessageProperties properties,
                                                     ExpressionParser parser,
                                                     ParserContext parserContext) {
        log.info("ExpressionPrefix is {}, ExpressionSuffix is {}", parserContext.getExpressionPrefix(), parserContext.getExpressionSuffix());
        return new SpElErrorMessageResolver(errorMessageSource, properties.getDefaultLocale(), parser, parserContext);
    }

    @Bean
    public ApiErrorLogger apiErrorLogger(ErrorMessageResolver errorMessageResolver, ObjectMapper objectMapper) {
        return new Slf4jApiErrorLogger(errorMessageResolver, objectMapper);
    }

    @Bean
    public ApiErrorFactory apiErrorFactory() {
        return new ApiErrorFactory();
    }
}