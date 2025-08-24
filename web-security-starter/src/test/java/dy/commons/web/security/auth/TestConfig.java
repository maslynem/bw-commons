package dy.commons.web.security.auth;

import com.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import com.digitalyard.commons.rest.exception.handler.logger.ApiErrorLogger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dy.commons.web.security.config.properties.WebSecurityProperties;
import dy.commons.web.security.service.PubKeyLoader;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@TestConfiguration
@Slf4j
public class TestConfig {
    public static final String SECURE_URL = "/test/secure";
    public static final String UNSECURE_URL = "/test/unsecure";
    public static final String ADMIN_URL = "/test/admin";

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }

    @Bean
    public ApiErrorFactory apiErrorFactory() {
        return new ApiErrorFactory();
    }

    @Bean
    public ApiErrorLogger apiErrorLogger() {
        return apiError -> log.debug(apiError.toString());
    }

    @Bean
    public KeyPair keyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    @Bean
    public PrivateKey privateKey(KeyPair keyPair) {
        return keyPair.getPrivate();
    }

    @Bean
    public PubKeyLoader pubKeyLoader(KeyPair keyPair) {
        PubKeyLoader loader = Mockito.mock(PubKeyLoader.class);
        PublicKey publicKey = keyPair.getPublic();
        Mockito.when(loader.loadPubKey(Mockito.anyString())).thenReturn(publicKey);
        return loader;
    }

    @Bean
    public WebSecurityProperties webSecurityProperties() {
        WebSecurityProperties props = new WebSecurityProperties();
        props.setPermissions(new String[]{UNSECURE_URL}); // или через setter
        return props;
    }

    @RestController
    static class TestController {
        @GetMapping(SECURE_URL)
        public String secure() {
            return "ok";
        }

        @GetMapping(UNSECURE_URL)
        public String unsecure() {
            return "ok";
        }

        @GetMapping(ADMIN_URL)
        @PreAuthorize("hasRole('ADMIN')")
        public String admin() {
            return "admin";
        }
    }
}
