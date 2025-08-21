package dy.commons.web.security.service.impl;

import dy.commons.web.security.config.model.AuthConfig;
import dy.commons.web.security.exception.JwtExpiredException;
import dy.commons.web.security.exception.JwtValidationException;
import dy.commons.web.security.service.JwtService;
import dy.commons.web.security.service.PubKeyLoader;
import exception.model.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.PublicKey;

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
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException(ErrorCode.EXPIRED_TOKEN.name());
        } catch (JwtException e) {
            throw new JwtValidationException(ErrorCode.INVALID_TOKEN.name());
        }
    }
}