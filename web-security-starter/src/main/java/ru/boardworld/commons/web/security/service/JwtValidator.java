package ru.boardworld.commons.web.security.service;

import io.jsonwebtoken.Claims;

public interface JwtValidator {
    Claims validateToken(String token);
}
