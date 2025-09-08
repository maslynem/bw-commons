package ru.boardworld.commons.rest.exception.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.boardworld.commons.rest.exception.logger.ApiErrorLogger;
import ru.boardworld.commons.rest.exception.logger.Slf4jApiErrorLogger;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ApiErrorConfiguration {

    @Bean
    public ApiErrorLogger apiErrorLogger(ObjectMapper objectMapper) {
        return new Slf4jApiErrorLogger(objectMapper);
    }

}