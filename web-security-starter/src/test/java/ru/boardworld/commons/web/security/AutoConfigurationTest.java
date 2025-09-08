package ru.boardworld.commons.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.boardworld.commons.rest.exception.handler.ApiErrorFactory;
import ru.boardworld.commons.rest.exception.logger.ApiErrorLogger;
import ru.boardworld.commons.web.security.config.CorsAutoConfiguration;
import ru.boardworld.commons.web.security.config.JwtAutoConfiguration;
import ru.boardworld.commons.web.security.config.SecurityAutoConfiguration;
import ru.boardworld.commons.web.security.service.JwtValidator;
import ru.boardworld.commons.web.security.service.PubKeyLoader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

class AutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    JwtAutoConfiguration.class,
                    CorsAutoConfiguration.class,
                    SecurityAutoConfiguration.class))
            // указываем реальный файл сертификата из ресурсов модуля
            .withPropertyValues(
                    "bw.auth.public-key-path=classpath:test-public-key.crt",
                    "bw.web-security.permissions[0]=/actuator/health"
            )
            .withBean(ObjectMapper.class, ObjectMapper::new)
            .withBean(ApiErrorFactory.class, () -> Mockito.mock(ApiErrorFactory.class))
            .withBean(ApiErrorLogger.class, () -> Mockito.mock(ApiErrorLogger.class))
            ;

    @Test
    void contextLoadsAndProvidesExpectedBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();

            assertThat(context).hasSingleBean(PubKeyLoader.class);
            assertThat(context).hasSingleBean(JwtValidator.class);
            assertThat(context).hasSingleBean(CorsConfiguration.class);
            assertThat(context).hasSingleBean(CorsConfigurationSource.class);

            assertThat(context).hasSingleBean(SecurityFilterChain.class);
            SecurityFilterChain chain = context.getBean(SecurityFilterChain.class);
            assertThat(chain).isNotNull();

            CorsConfiguration cors = context.getBean(CorsConfiguration.class);
            assertThat(cors.getAllowedMethods()).isNotEmpty();
            assertThat(cors.getMaxAge()).isNotNull();
        });
    }
}
