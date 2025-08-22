package com.digitalyard.commons.rest.exception.config;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Locale;

@Data
@ConfigurationProperties(prefix = "error.messages")
public class ErrorMessageProperties {

    private Duration cacheTimeout = Duration.ofSeconds(-1);

    private Locale defaultLocale = Locale.ENGLISH;

}