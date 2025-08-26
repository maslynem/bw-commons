package dy.digitalyard.commons.web.security.service.impl;

import dy.digitalyard.commons.web.security.config.properties.AuthProperties;
import dy.digitalyard.commons.web.security.exception.token.InvalidTokenException;
import dy.digitalyard.commons.web.security.exception.token.TokenExpiredException;
import dy.digitalyard.commons.web.security.service.PubKeyLoader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultJwtServiceTest {

    private static final String SUBJECT = UUID.randomUUID().toString();
    private static final String LOGIN = "test_login";

    @Test
    void validateAccessToken_success_withRealRSA() throws Exception {
        KeyPair kp = generateRsaKeyPair();
        PrivateKey privateKey = kp.getPrivate();
        PublicKey publicKey = kp.getPublic();

        String token = Jwts.builder()
                .setSubject(SUBJECT)
                .claim("LOGIN", LOGIN)
                .setExpiration(new Date(System.currentTimeMillis() + 60_000)) // +60s
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        PubKeyLoader loader = mock(PubKeyLoader.class);
        when(loader.loadPubKey("classpath:some")).thenReturn(publicKey);

        AuthProperties props = new AuthProperties();
        props.setPublicKeyPath("classpath:some");

        DefaultJwtService service = new DefaultJwtService(props, loader);

        Claims claims = service.validateAccessToken(token);
        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(SUBJECT);
        assertThat(claims.get("LOGIN", String.class)).isEqualTo(LOGIN);
    }

    @Test
    void validateAccessToken_throwsTokenExpiredException_forExpiredToken() throws Exception {
        KeyPair kp = generateRsaKeyPair();
        PrivateKey privateKey = kp.getPrivate();
        PublicKey publicKey = kp.getPublic();

        String token = Jwts.builder()
                .setSubject(SUBJECT)
                .claim("LOGIN", LOGIN)
                .setExpiration(new Date(System.currentTimeMillis() - 10_000)) // expired 10s ago
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        PubKeyLoader loader = mock(PubKeyLoader.class);
        when(loader.loadPubKey("classpath:some")).thenReturn(publicKey);

        AuthProperties props = new AuthProperties();
        props.setPublicKeyPath("classpath:some");

        DefaultJwtService service = new DefaultJwtService(props, loader);

        assertThatThrownBy(() -> service.validateAccessToken(token))
                .isInstanceOf(TokenExpiredException.class);
    }

    @Test
    void validateAccessToken_throwsInvalidTokenException_forWrongSignature() throws Exception {
        // Создадим два keypair — подпишем одним, а при валидации дадим другой публичный ключ
        KeyPair kpSigner = generateRsaKeyPair();
        PrivateKey privateKey = kpSigner.getPrivate();

        KeyPair kpVerifier = generateRsaKeyPair();
        PublicKey otherPublicKey = kpVerifier.getPublic();

        String token = Jwts.builder()
                .setSubject(SUBJECT)
                .claim("LOGIN", LOGIN)
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

        PubKeyLoader loader = mock(PubKeyLoader.class);
        when(loader.loadPubKey("classpath:some")).thenReturn(otherPublicKey);

        AuthProperties props = new AuthProperties();
        props.setPublicKeyPath("classpath:some");

        DefaultJwtService service = new DefaultJwtService(props, loader);

        assertThatThrownBy(() -> service.validateAccessToken(token))
                .isInstanceOf(InvalidTokenException.class);
    }

    private KeyPair generateRsaKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        return kpg.generateKeyPair();
    }
}
