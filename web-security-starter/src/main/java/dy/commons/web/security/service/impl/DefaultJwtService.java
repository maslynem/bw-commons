package dy.commons.web.security.service.impl;

import dy.commons.web.security.config.properties.AuthProperties;
import dy.commons.web.security.exception.TokenExpiredException;
import dy.commons.web.security.exception.InvalidTokenException;
import dy.commons.web.security.service.JwtService;
import dy.commons.web.security.service.PubKeyLoader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.PublicKey;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class DefaultJwtService implements JwtService {

    private final PublicKey pubKey;

    public DefaultJwtService(AuthProperties authProperties, PubKeyLoader pubKeyLoader) {
        this.pubKey = pubKeyLoader.loadPubKey(authProperties.getPublicKeyPath());
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
            Date expiredAt = e.getClaims().getExpiration();
            throw new TokenExpiredException(token, expiredAt.toInstant());
        } catch (JwtException e) {
            throw new InvalidTokenException(token, e);
        }
    }
}