package ru.boardworld.commons.web.security.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import ru.boardworld.commons.web.security.config.properties.AuthProperties;
import ru.boardworld.commons.web.security.model.user.AuthenticatedUser;
import ru.boardworld.commons.web.security.model.user.Constants;
import ru.boardworld.commons.web.security.model.user.Role;
import ru.boardworld.commons.web.security.service.JwtCreator;
import ru.boardworld.commons.web.security.service.PrivateKeyLoader;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtCreatorImpl implements JwtCreator {

    private final PrivateKey privateKey;
    private final AuthProperties authProperties;

    public JwtCreatorImpl(PrivateKeyLoader privateKeyLoader, AuthProperties authProperties) {
        this.privateKey = privateKeyLoader.loadPrivateKey(authProperties.getPrivateKeyPath());
        this.authProperties = authProperties;
    }


    @Override
    public String generateAccessToken(AuthenticatedUser authenticatedUser) {
        Instant now = Instant.now();
        Instant expiry = now.plus(authProperties.getAccessTokenTtl());
        Claims claims = generateClaims(authenticatedUser);
        return Jwts.builder()
                .setSubject(authenticatedUser.getId().toString())
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(AuthenticatedUser authenticatedUser) {
        Instant now = Instant.now();
        Instant expiry = now.plus(authProperties.getRefreshTokenTtl());

        return Jwts.builder()
                .setSubject(authenticatedUser.getId().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Claims generateClaims(AuthenticatedUser authenticatedUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.USERNAME.name(), authenticatedUser.getUsername());
        List<String> roles = authenticatedUser.getRoles().stream().map(Role::getName).toList();
        claims.put(Constants.USER_RIGHTS.name(), roles);
        return new DefaultClaims(claims);
    }

}
