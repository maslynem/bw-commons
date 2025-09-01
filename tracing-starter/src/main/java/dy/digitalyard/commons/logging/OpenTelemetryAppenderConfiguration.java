package dy.digitalyard.commons.logging;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.springframework.boot.actuate.autoconfigure.opentelemetry.OpenTelemetryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@AutoConfigureAfter(OpenTelemetryAutoConfiguration.class)
@ConditionalOnClass({OpenTelemetryAppender.class, OpenTelemetry.class})
public class OpenTelemetryAppenderConfiguration {
    @Bean
    OpenTelemetryAppenderInitializer openTelemetryAppenderInitializer(OpenTelemetry openTelemetry) {
        return new OpenTelemetryAppenderInitializer(openTelemetry);
    }
}
