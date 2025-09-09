package ru.boardworld.commons.web.security.service.impl;

import ru.boardworld.commons.web.security.config.properties.AuthProperties;
import ru.boardworld.commons.web.security.exception.token.InvalidTokenException;
import ru.boardworld.commons.web.security.exception.token.TokenExpiredException;
import ru.boardworld.commons.web.security.service.JwtValidator;
import ru.boardworld.commons.web.security.service.PublicKeyLoader;
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
public class DefaultJwtValidator implements JwtValidator {

    private final PublicKey pubKey;

    public DefaultJwtValidator(AuthProperties authProperties, PublicKeyLoader publicKeyLoader) {
        this.pubKey = publicKeyLoader.loadPublicKey(authProperties.getPublicKeyPath());
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
            throw new TokenExpiredException(expiredAt.toInstant());
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }
}