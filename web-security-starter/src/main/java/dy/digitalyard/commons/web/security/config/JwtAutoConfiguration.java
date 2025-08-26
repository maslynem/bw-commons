package dy.digitalyard.commons.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dy.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import dy.digitalyard.commons.rest.exception.logger.ApiErrorLogger;
import dy.digitalyard.commons.web.security.auth.ForbiddenEntryPoint;
import dy.digitalyard.commons.web.security.auth.JwtAuthenticationEntryPoint;
import dy.digitalyard.commons.web.security.auth.JwtAuthenticationProvider;
import dy.digitalyard.commons.web.security.config.properties.AuthProperties;
import dy.digitalyard.commons.web.security.service.JwtService;
import dy.digitalyard.commons.web.security.service.JwtUserProvider;
import dy.digitalyard.commons.web.security.service.PubKeyLoader;
import dy.digitalyard.commons.web.security.service.impl.DefaultJwtService;
import dy.digitalyard.commons.web.security.service.impl.JwtUserProviderImpl;
import dy.digitalyard.commons.web.security.service.impl.X509PubKeyLoader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class JwtAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public PubKeyLoader pubKeyLoader(ResourceLoader resourceLoader) {
        return new X509PubKeyLoader(resourceLoader);
    }


    @Bean
    public JwtService jwtService(AuthProperties authProperties, PubKeyLoader pubKeyLoader) {
        return new DefaultJwtService(authProperties, pubKeyLoader);
    }

    @Bean
    public JwtUserProvider userProvider(JwtService jwtService) {
        return new JwtUserProviderImpl(jwtService);
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider(JwtUserProvider userProvider) {
        return new JwtAuthenticationProvider(userProvider);
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