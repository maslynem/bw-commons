package dy.digitalyard.commons.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dy.digitalyard.commons.rest.exception.config.ApiErrorConfiguration;
import dy.digitalyard.commons.rest.exception.config.ExceptionHandlerConfiguration;
import dy.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import dy.digitalyard.commons.rest.exception.handler.GlobalExceptionHandler;
import dy.digitalyard.commons.rest.exception.logger.ApiErrorLogger;
import dy.digitalyard.commons.rest.exception.mapper.ExceptionDetailsMapperRegistry;
import dy.digitalyard.commons.rest.jackson.ObjectMapperConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class AutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    ApiErrorConfiguration.class,
                    ObjectMapperConfiguration.class,
                    ExceptionHandlerConfiguration.class));


    @Test
    void contextLoads_and_beansPresent() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).hasSingleBean(ObjectMapper.class);
            assertThat(context).hasSingleBean(ApiErrorLogger.class);
            assertThat(context).hasSingleBean(ApiErrorFactory.class);
            assertThat(context).hasSingleBean(GlobalExceptionHandler.class);
            assertThat(context).hasSingleBean(ExceptionDetailsMapperRegistry.class);
        });

    }
}