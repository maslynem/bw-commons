package ru.boardworld.commons.web.security.utils;

import ru.boardworld.commons.web.security.model.user.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.UUID;

import static ru.boardworld.commons.web.security.model.user.Constants.MIDDLE_NAME;

@UtilityClass
public class ClaimsUtils {
    public static final String M_ID = UUID.randomUUID().toString();
    public static final String M_LOGIN = "mock_login";
    public static final String M_FIRST_NAME = "mock_fn";
    public static final String M_MIDDLE_NAME = "mock_ml";
    public static final String M_LAST_NAME = "mock_ln";
    public static final List<String> M_ROLES = List.of("ROLE_USER");

    public static Claims createMockClaims(boolean locked, boolean deleted) {
        Claims claims = Jwts.claims();
        claims.setSubject(M_ID);
        claims.put(Constants.LOGIN.name(), M_LOGIN);
        claims.put(Constants.FIRST_NAME.name(), M_FIRST_NAME);
        claims.put(MIDDLE_NAME.name(), M_MIDDLE_NAME);
        claims.put(Constants.LAST_NAME.name(), M_LAST_NAME);
        claims.put(Constants.USER_RIGHTS.name(), M_ROLES);
        claims.put(Constants.LOCKED.name(), locked);
        claims.put(Constants.DELETED.name(), deleted);
        return claims;
    }
}
