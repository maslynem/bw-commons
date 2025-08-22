package dy.commons.web.security.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    Claims validateAccessToken(String token);
}
