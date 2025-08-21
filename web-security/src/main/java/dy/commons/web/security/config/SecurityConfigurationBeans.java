package dy.commons.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dy.commons.web.security.auth.JwtAuthenticationEntryPoint;
import dy.commons.web.security.auth.JwtAuthenticationProvider;
import dy.commons.web.security.config.model.AuthConfig;
import dy.commons.web.security.config.model.PermissionUrl;
import dy.commons.web.security.service.JwtUserProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfigurationBeans {

    @Bean
    @ConfigurationProperties(prefix = "dy.auth")
    public AuthConfig authConfig() {
        return new AuthConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "dy.web-security")
    public PermissionUrl permissionUrl() {
        return new PermissionUrl();
    }

    @Bean
    public Http403ForbiddenEntryPoint http403ForbiddenEntryPoint() {
        return new Http403ForbiddenEntryPoint();
    }

    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    @ConfigurationProperties(prefix = "dy.web-security.cors")
    public CorsConfiguration corsConfiguration() { // TODO разобраться
        return new CorsConfiguration();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider(JwtUserProvider userProvider) {
        return new JwtAuthenticationProvider(userProvider);
    }
}