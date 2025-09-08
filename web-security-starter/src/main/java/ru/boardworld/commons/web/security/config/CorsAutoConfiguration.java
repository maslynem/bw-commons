package ru.boardworld.commons.web.security.config;

import ru.boardworld.commons.web.security.config.properties.WebSecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@AutoConfiguration
@EnableConfigurationProperties(WebSecurityProperties.class)
public class CorsAutoConfiguration {

    @Bean
    public CorsConfiguration corsConfiguration(WebSecurityProperties webSecurityProperties) {
        CorsConfiguration configuration = new CorsConfiguration();

        WebSecurityProperties.Cors corsProps = webSecurityProperties.getCors();

        configuration.setAllowedOrigins(Arrays.asList(corsProps.getAllowedOrigins()));
        configuration.setAllowedMethods(Arrays.asList(corsProps.getAllowedMethods()));
        configuration.setAllowedHeaders(Arrays.asList(corsProps.getAllowedHeaders()));
        configuration.setAllowCredentials(corsProps.getAllowCredentials());
        configuration.setMaxAge(corsProps.getMaxAge().getSeconds());

        return configuration;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(CorsConfiguration corsConfiguration) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}