package ru.boardworld.commons.web.security.utils;

import ru.boardworld.commons.web.security.model.user.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.UUID;


@UtilityClass
public class ClaimsUtils {
    public static final String M_ID = UUID.randomUUID().toString();
    public static final String M_USERNAME = "mock_login";
    public static final List<String> M_ROLES = List.of("ROLE_USER");

    public static Claims createMockClaims() {
        Claims claims = Jwts.claims();
        claims.setSubject(M_ID);
        claims.put(Constants.USERNAME.name(), M_USERNAME);
        claims.put(Constants.USER_RIGHTS.name(), M_ROLES);
        return claims;
    }
}
