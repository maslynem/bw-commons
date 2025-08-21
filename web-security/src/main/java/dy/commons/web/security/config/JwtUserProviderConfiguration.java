package dy.commons.web.security.config;

import dy.commons.web.security.config.model.AuthConfig;
import dy.commons.web.security.service.JwtService;
import dy.commons.web.security.service.PubKeyLoader;
import dy.commons.web.security.service.impl.DefaultJwtService;
import dy.commons.web.security.service.impl.X509PubKeyLoader;
import dy.commons.web.security.service.JwtUserProvider;
import dy.commons.web.security.service.impl.JwtUserProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class JwtUserProviderConfiguration {

    @Bean
    public PubKeyLoader pubKeyLoader(ResourceLoader resourceLoader) {
        return new X509PubKeyLoader(resourceLoader);
    }


    @Bean
    public JwtService jwtService(AuthConfig authConfig, PubKeyLoader pubKeyLoader) {
        return new DefaultJwtService(authConfig, pubKeyLoader);
    }

    @Bean
    public JwtUserProvider userProvider(JwtService jwtService) {
        return new JwtUserProviderImpl(jwtService);
    }
}