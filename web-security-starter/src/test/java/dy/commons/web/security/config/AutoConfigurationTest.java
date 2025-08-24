package dy.commons.web.security.config;

import com.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import com.digitalyard.commons.rest.exception.handler.logger.ApiErrorLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import dy.commons.web.security.service.JwtService;
import dy.commons.web.security.service.JwtUserProvider;
import dy.commons.web.security.service.PubKeyLoader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

class AutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    JwtAutoConfiguration.class,
                    CorsAutoConfiguration.class,
                    SecurityAutoConfiguration.class))
            // указываем реальный файл сертификата из ресурсов модуля
            .withPropertyValues(
                    "dy.auth.public-key-path=classpath:test-public-key.crt",
                    "dy.web-security.permissions[0]=/actuator/health"
            )
            .withBean(ObjectMapper.class, ObjectMapper::new)
            .withBean(ApiErrorFactory.class, () -> Mockito.mock(ApiErrorFactory.class))
            .withBean(ApiErrorLogger.class, () -> Mockito.mock(ApiErrorLogger.class));

    @Test
    void contextLoadsAndProvidesExpectedBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            // базовые сервисы
            assertThat(context).hasBean("pubKeyLoader");
            assertThat(context).hasBean("jwtService");
            assertThat(context).hasBean("userProvider");

            assertThat(context).hasBean("jwtAuthenticationProvider");
            assertThat(context).hasBean("accessDeniedHandler");
            assertThat(context).hasBean("jwtAuthenticationEntryPoint");

            // типовые проверки
            assertThat(context).hasSingleBean(PubKeyLoader.class);
            assertThat(context).hasSingleBean(JwtService.class);
            assertThat(context).hasSingleBean(JwtUserProvider.class);
            assertThat(context).hasBean("corsConfiguration");
            assertThat(context).hasBean("corsConfigurationSource");

            // SecurityFilterChain присутствует
            assertThat(context).hasSingleBean(SecurityFilterChain.class);
            SecurityFilterChain chain = context.getBean(SecurityFilterChain.class);
            assertThat(chain).isNotNull();

            // Cors configuration sanity check
            CorsConfiguration cors = context.getBean(CorsConfiguration.class);
            assertThat(cors.getAllowedMethods()).isNotEmpty();
            assertThat(cors.getMaxAge()).isNotNull();
        });
    }
}
