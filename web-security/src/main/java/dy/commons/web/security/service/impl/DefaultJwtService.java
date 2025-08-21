package dy.commons.web.security.service.impl;

import dy.commons.web.security.config.model.AuthConfig;
import dy.commons.web.security.service.JwtService;
import dy.commons.web.security.service.PubKeyLoader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;

import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

@Slf4j
@RequiredArgsConstructor
public class DefaultJwtService implements JwtService {

    private final PublicKey pubKey;

    public DefaultJwtService(AuthConfig authConfig, PubKeyLoader pubKeyLoader) {
        this.pubKey = pubKeyLoader.loadPubKey(authConfig.getPublicKeyPath());
    }

    @Override
    public Claims validateAccessToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(this.pubKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate JWT token", e); // TODO
        }
    }
}