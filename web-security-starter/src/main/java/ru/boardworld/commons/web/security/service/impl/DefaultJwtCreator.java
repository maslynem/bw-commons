package ru.boardworld.commons.web.security.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import ru.boardworld.commons.web.security.config.properties.AuthProperties;
import ru.boardworld.commons.web.security.dto.AccessTokenDto;
import ru.boardworld.commons.web.security.dto.RefreshTokenDto;
import ru.boardworld.commons.web.security.model.user.AuthenticatedUser;
import ru.boardworld.commons.web.security.model.user.Constants;
import ru.boardworld.commons.web.security.model.user.Role;
import ru.boardworld.commons.web.security.service.JwtCreator;
import ru.boardworld.commons.web.security.service.PrivateKeyLoader;

import java.security.PrivateKey;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultJwtCreator implements JwtCreator {

    private final PrivateKey privateKey;
    private final AuthProperties authProperties;

    public DefaultJwtCreator(PrivateKeyLoader privateKeyLoader, AuthProperties authProperties) {
        this.privateKey = privateKeyLoader.loadPrivateKey(authProperties.getPrivateKeyPath());
        this.authProperties = authProperties;
    }


    @Override
    public AccessTokenDto generateAccessToken(AuthenticatedUser authenticatedUser) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime expiry = now.plus(authProperties.getAccessTokenTtl());
        Claims claims = generateClaims(authenticatedUser);


        String token = Jwts.builder()
                .setSubject(authenticatedUser.getId().toString())
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(expiry.toInstant()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
        return AccessTokenDto
                .builder()
                .token(token)
                .createdAt(now)
                .expiresAt(expiry)
                .build();
    }

    @Override
    public RefreshTokenDto generateRefreshToken(AuthenticatedUser authenticatedUser) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime expiry = now.plus(authProperties.getRefreshTokenTtl());

        String token = Jwts.builder()
                .setSubject(authenticatedUser.getId().toString())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(expiry.toInstant()))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
        return RefreshTokenDto
                .builder()
                .token(token)
                .createdAt(now)
                .expiresAt(expiry)
                .build();
    }

    public Claims generateClaims(AuthenticatedUser authenticatedUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.USERNAME.name(), authenticatedUser.getUsername());
        List<String> roles = authenticatedUser.getRoles().stream().map(Role::getName).toList();
        claims.put(Constants.USER_RIGHTS.name(), roles);
        return new DefaultClaims(claims);
    }

}
