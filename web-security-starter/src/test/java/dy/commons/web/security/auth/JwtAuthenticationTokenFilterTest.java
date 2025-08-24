package dy.commons.web.security.auth;

import dy.commons.web.security.utils.ClaimsUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = {
                TestConfig.class,
                dy.commons.web.security.config.SecurityAutoConfiguration.class,
                dy.commons.web.security.config.JwtAutoConfiguration.class
        },
        properties = {
                "dy.web-security.permissions[0]=/test/unsecure",
                "logging.level.dy.commons.web.security=DEBUG"
        }
)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
@Tag("integration")
public class JwtAuthenticationTokenFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PrivateKey privateKey;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void whenValidToken_thenAccessSecuredEndpoint() throws Exception {
        String token = buildToken(privateKey, builder -> builder
                .setClaims(ClaimsUtils.createMockClaims(false, false))
                .setSubject(UUID.randomUUID().toString())
                .setExpiration(Date.from(Instant.now().plusSeconds(60))));

        mockMvc.perform(get(TestConfig.SECURE_URL)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void whenNoToken_thenAccessUnsecuredEndpoint() throws Exception {
        mockMvc.perform(get(TestConfig.UNSECURE_URL))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void whenExpiredToken_thenUnauthorizedApiError() throws Exception {
        String token = buildToken(privateKey, builder -> builder
                .setClaims(ClaimsUtils.createMockClaims(false, false))
                .setExpiration(Date.from(Instant.now().minusSeconds(10))));

        mockMvc.perform(get(TestConfig.SECURE_URL)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"code\":\"TOKEN_EXPIRED\"}", false));
    }

    @Test
    void whenNoToken_thenUnauthorizedApiError() throws Exception {
        mockMvc.perform(get(TestConfig.SECURE_URL))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"code\":\"UNAUTHORIZED\"}", false));
    }

    @Test
    void whenTokenSignedByOtherKey_thenUnauthorizedApiError() throws Exception {
        KeyPair other = generateRsa();
        String token = buildToken(other.getPrivate(), builder -> builder
                .setClaims(ClaimsUtils.createMockClaims(false, false))
                .setExpiration(Date.from(Instant.now().plusSeconds(60))));

        mockMvc.perform(get(TestConfig.SECURE_URL)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"code\":\"INVALID_TOKEN\"}", false));
    }

    @Test
    void whenValidToken_butUserLocked_thenForbiddenApiError() throws Exception {
        String token = buildToken(privateKey, builder -> builder
                .setClaims(ClaimsUtils.createMockClaims(true, false))
                .setExpiration(Date.from(Instant.now().plusSeconds(60))));

        mockMvc.perform(get(TestConfig.SECURE_URL)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"code\":\"BLOCKED_USER\"}", false));
    }

    @Test
    void whenValidToken_butUserDeleted_thenUnauthorizedApiError() throws Exception {
        String token = buildToken(privateKey, builder -> builder
                .setClaims(ClaimsUtils.createMockClaims(false, true))
                .setExpiration(Date.from(Instant.now().plusSeconds(60))));

        mockMvc.perform(get(TestConfig.SECURE_URL)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"code\":\"USER_DELETED_OR_DOES_NOT_EXIST\"}", false));
    }

    @Test
    void whenValidToken_butNotEnoughRights_thenForbiddenApiError() throws Exception {
        String token = buildToken(privateKey, builder -> builder
                .setClaims(ClaimsUtils.createMockClaims(false, false))
                .setExpiration(Date.from(Instant.now().plusSeconds(60))));

        mockMvc.perform(get(TestConfig.ADMIN_URL)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"code\":\"FORBIDDEN\"}", false));
    }

    private String buildToken(PrivateKey privateKey, Consumer<JwtBuilder> consumer) {
        JwtBuilder builder = Jwts.builder();
        consumer.accept(builder);
        return builder.signWith(privateKey, SignatureAlgorithm.RS256).compact();
    }

    private static KeyPair generateRsa() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }
}
