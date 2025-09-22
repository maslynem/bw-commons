package ru.boardworld.commons.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.access.AccessDeniedHandler;
import ru.boardworld.commons.rest.exception.handler.ApiErrorFactory;
import ru.boardworld.commons.rest.exception.logger.ApiErrorLogger;
import ru.boardworld.commons.web.security.auth.ForbiddenEntryPoint;
import ru.boardworld.commons.web.security.auth.JwtAuthenticationEntryPoint;
import ru.boardworld.commons.web.security.auth.JwtAuthenticationProvider;
import ru.boardworld.commons.web.security.config.properties.AuthProperties;
import ru.boardworld.commons.web.security.service.JwtValidator;
import ru.boardworld.commons.web.security.service.PublicKeyLoader;
import ru.boardworld.commons.web.security.service.impl.DefaultJwtValidator;
import ru.boardworld.commons.web.security.service.impl.PemKeyLoader;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
@AutoConfigureBefore(SecurityAutoConfiguration.class)
public class JwtAutoConfiguration {

    @Bean
    public PemKeyLoader pemKeyLoader(ResourceLoader resourceLoader) {
        return new PemKeyLoader(resourceLoader);
    }

    @Bean
    public JwtValidator jwtValidator(AuthProperties authProperties, PublicKeyLoader publicKeyLoader) {
        return new DefaultJwtValidator(authProperties, publicKeyLoader);
    }


    @Bean
    public AuthenticationProvider jwtAuthenticationProvider(JwtValidator jwtValidator) {
        return new JwtAuthenticationProvider(jwtValidator);
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper, ApiErrorFactory apiErrorFactory, ApiErrorLogger apiErrorLogger) {
        return new JwtAuthenticationEntryPoint(apiErrorFactory, apiErrorLogger, objectMapper);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper, ApiErrorLogger apiErrorLogger, ApiErrorFactory apiErrorFactory) {
        return new ForbiddenEntryPoint(apiErrorFactory, apiErrorLogger, objectMapper);
    }


}