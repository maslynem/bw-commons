package dy.digitalyard.commons.rest.exception.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dy.digitalyard.commons.rest.exception.logger.ApiErrorLogger;
import dy.digitalyard.commons.rest.exception.logger.Slf4jApiErrorLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiErrorConfiguration {

    @Bean
    public ApiErrorLogger apiErrorLogger(ObjectMapper objectMapper) {
        return new Slf4jApiErrorLogger(objectMapper);
    }

}