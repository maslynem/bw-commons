package dy.commons.web.security.service.impl;

import dy.commons.web.security.exception.token.InvalidTokenException;
import dy.commons.web.security.model.JwtAuthenticationToken;
import dy.commons.web.security.model.user.AuthenticatedUser;
import dy.commons.web.security.model.user.Constants;
import dy.commons.web.security.model.user.Role;
import dy.commons.web.security.service.JwtService;
import dy.commons.web.security.service.JwtUserProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtUserProviderImpl implements JwtUserProvider {

    private final JwtService jwtService;

    @Override
    public AuthenticatedUser getUser(JwtAuthenticationToken jwtAuthenticationToken) {
        log.debug("Validating JWT: {}", jwtAuthenticationToken.getJwtToken());

        Claims claims = jwtService.validateAccessToken(jwtAuthenticationToken.getJwtToken());
        log.debug("JWT claims were extracted: {}", claims);

        UUID id = UUID.fromString(getRequiredClaim(claims, Claims.SUBJECT, String.class));
        String login = getRequiredClaim(claims, Constants.LOGIN.name(), String.class);
        // TODO: Как проверить, что пользователь не был заблокирован после выдачи jwt-токена.

        String firstName = getRequiredClaim(claims, Constants.FIRST_NAME.name(), String.class);
        String middleName = getRequiredClaim(claims, Constants.MIDDLE_NAME.name(), String.class);
        String lastName = getRequiredClaim(claims, Constants.LAST_NAME.name(), String.class);
        @SuppressWarnings("unchecked")
        List<String> rolesString = getRequiredClaim(claims, Constants.USER_RIGHTS.name(), List.class);
        List<Role> rolesList = rolesString.stream()
                .map(Role::new)
                .toList();
        Boolean locked = getRequiredClaim(claims, Constants.LOCKED.name(), Boolean.class);
        Boolean deleted = getRequiredClaim(claims, Constants.DELETED.name(), Boolean.class);

        return AuthenticatedUser.builder()
                .id(id)
                .login(login)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .roles(rolesList)
                .locked(locked)
                .deleted(deleted)
                .build();
    }

    private <T> T getRequiredClaim(Claims claims, String claimKey, Class<T> type) {
        T value = claims.get(claimKey, type);
        if (value == null) {
            throw new InvalidTokenException("JWT token is missing required claim: " + claimKey);
        }
        return value;
    }
}
