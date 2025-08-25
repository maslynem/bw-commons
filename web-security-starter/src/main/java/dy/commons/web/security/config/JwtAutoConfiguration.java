package dy.commons.web.security.config;

import com.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import dy.commons.web.security.auth.ForbiddenEntryPoint;
import dy.commons.web.security.auth.JwtAuthenticationEntryPoint;
import dy.commons.web.security.auth.JwtAuthenticationProvider;
import dy.commons.web.security.config.properties.AuthProperties;
import dy.commons.web.security.service.JwtService;
import dy.commons.web.security.service.JwtUserProvider;
import dy.commons.web.security.service.PubKeyLoader;
import dy.commons.web.security.service.impl.DefaultJwtService;
import dy.commons.web.security.service.impl.JwtUserProviderImpl;
import dy.commons.web.security.service.impl.X509PubKeyLoader;
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
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper, ApiErrorFactory apiErrorFactory) {
        return new JwtAuthenticationEntryPoint(apiErrorFactory, objectMapper);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(ObjectMapper objectMapper, ApiErrorFactory apiErrorFactory) {
        return new ForbiddenEntryPoint(apiErrorFactory, objectMapper);
    }

}