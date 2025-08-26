package dy.digitalyard.commons.web.security.auth;

import dy.digitalyard.commons.web.security.exception.token.InvalidTokenException;
import dy.digitalyard.commons.web.security.model.JwtAuthenticationToken;
import dy.digitalyard.commons.web.security.model.user.AuthenticatedUser;
import dy.digitalyard.commons.web.security.model.user.Constants;
import dy.digitalyard.commons.web.security.model.user.Role;
import dy.digitalyard.commons.web.security.service.JwtValidator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtValidator jwtValidator;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

        AuthenticatedUser authenticatedUser = getUser(jwtAuthenticationToken);
        if (authenticatedUser == null) {
            throw new InvalidTokenException("User not found");
        }

        return new JwtAuthenticationToken(jwtAuthenticationToken.getJwtToken(), authenticatedUser);
    }

    private AuthenticatedUser getUser(JwtAuthenticationToken jwtAuthenticationToken) {
        log.debug("Validating JWT");

        Claims claims = jwtValidator.validateAccessToken(jwtAuthenticationToken.getJwtToken());
        log.debug("JWT claims were extracted: {}", claims);

        UUID id = UUID.fromString(getRequiredClaim(claims, Claims.SUBJECT, String.class));
        String login = getRequiredClaim(claims, Constants.LOGIN.name(), String.class);
        String firstName = getRequiredClaim(claims, Constants.FIRST_NAME.name(), String.class);
        String middleName = getRequiredClaim(claims, Constants.MIDDLE_NAME.name(), String.class);
        String lastName = getRequiredClaim(claims, Constants.LAST_NAME.name(), String.class);
        @SuppressWarnings("unchecked")
        List<String> rolesString = getRequiredClaim(claims, Constants.USER_RIGHTS.name(), List.class);
        List<Role> rolesList = rolesString.stream()
                .map(Role::new)
                .toList();

        return AuthenticatedUser.builder()
                .id(id)
                .login(login)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .roles(rolesList)
                .build();
    }

    private <T> T getRequiredClaim(Claims claims, String claimKey, Class<T> type) {
        T value = claims.get(claimKey, type);
        if (value == null) {
            throw new InvalidTokenException("JWT token is missing required claim: " + claimKey);
        }
        return value;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
