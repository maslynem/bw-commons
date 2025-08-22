package dy.commons.web.security.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "dy.web-security")
@Validated
public class WebSecurityProperties {
    /**
     * List of URL patterns that are permitted without authentication
     */
    private String[] permissions = new String[0];

    private Cors cors = new Cors();


    @Data
    public static class Cors {

        /**
         * Comma-separated list of origins to allow
         */
        private String[] allowedOrigins = {"*"};

        /**
         * Comma-separated list of methods to allow
         */
        private String[] allowedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};

        /**
         * Comma-separated list of headers to allow
         */
        private String[] allowedHeaders = {"*"};

        /**
         * Whether credentials are supported
         */
        private Boolean allowCredentials = false;

        /**
         * How long the response from a pre-flight request can be cached
         */
        private Duration maxAge = Duration.ofHours(1);

    }
}
