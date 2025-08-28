package dy.digitalyard.commons.web.security.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@ConfigurationProperties(prefix = "dy.auth")
@Validated
public class AuthProperties {
    /**
     * Path to public key (X509) for JWT verification
     */
    @NotNull
    private String publicKeyPath = "classpath:public.key";
}
