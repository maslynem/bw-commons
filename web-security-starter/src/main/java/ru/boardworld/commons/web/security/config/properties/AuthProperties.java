package ru.boardworld.commons.web.security.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "bw.auth")
@Validated
public class AuthProperties {
    /**
     * Path to public key (X509) for JWT verification
     */
    @NotNull
    private String publicKeyPath = "classpath:public.key";

    @NotNull
    private String privateKeyPath = "classpath:private.key";

    private Duration accessTokenTtl = Duration.ofMinutes(15);

    private Duration refreshTokenTtl = Duration.ofDays(30);
}
