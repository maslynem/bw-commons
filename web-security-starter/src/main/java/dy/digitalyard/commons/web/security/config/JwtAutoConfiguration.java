package dy.digitalyard.commons.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dy.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import dy.digitalyard.commons.rest.exception.logger.ApiErrorLogger;
import dy.digitalyard.commons.web.security.auth.ForbiddenEntryPoint;
import dy.digitalyard.commons.web.security.auth.JwtAuthenticationEntryPoint;
import dy.digitalyard.commons.web.security.auth.JwtAuthenticationProvider;
import dy.digitalyard.commons.web.security.config.properties.AuthProperties;
import dy.digitalyard.commons.web.security.service.JwtValidator;
import dy.digitalyard.commons.web.security.service.PubKeyLoader;
import dy.digitalyard.commons.web.security.service.impl.DefaultJwtValidator;
import dy.digitalyard.commons.web.security.service.impl.X509PubKeyLoader;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.access.AccessDeniedHandler;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
public class JwtAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PubKeyLoader pubKeyLoader(ResourceLoader resourceLoader) {
        return new X509PubKeyLoader(resourceLoader);
    }


    @Bean
    public JwtValidator jwtValidator(AuthProperties authProperties, PubKeyLoader pubKeyLoader) {
        return new DefaultJwtValidator(authProperties, pubKeyLoader);
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