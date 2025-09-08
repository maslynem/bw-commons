package ru.boardworld.commons.logging.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "logging.appender")
public class LoggingAppenderProperties {

    private final Console console = new Console();
    private final File file = new File();
    private final Otlp otlp = new Otlp();

    @Getter
    @Setter
    private static abstract class AppenderProperty {
        /**
         * Включить/выключить ConsoleAppender.
         */
        private boolean enable = true;
    }

    public static class Console  {
    }

    public static class File extends AppenderProperty {
    }

    public static class Otlp extends AppenderProperty {

    }
}
