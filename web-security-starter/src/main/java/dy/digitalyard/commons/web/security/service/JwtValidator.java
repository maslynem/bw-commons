package dy.digitalyard.commons.web.security.service;

import io.jsonwebtoken.Claims;

public interface JwtValidator {
    Claims validateAccessToken(String token);
}
