package dy.commons.web.security.auth;

import dy.commons.web.security.exception.token.InvalidTokenException;
import dy.commons.web.security.exception.user.BlockedUserException;
import dy.commons.web.security.exception.user.UserDeletedOrDoesNotExistException;
import dy.commons.web.security.model.JwtAuthenticationToken;
import dy.commons.web.security.model.user.AuthenticatedUser;
import dy.commons.web.security.service.JwtUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtUserProvider userProvider;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

        AuthenticatedUser authenticatedUser = userProvider.getUser(jwtAuthenticationToken);
        if (authenticatedUser == null) {
            throw new InvalidTokenException("User not found");
        }
        if (authenticatedUser.isAccountLocked()) {
            throw new BlockedUserException(authenticatedUser.getLogin());
        }
        if (authenticatedUser.isDeleted()) {
            throw new UserDeletedOrDoesNotExistException(authenticatedUser.getLogin());
        }

        return new JwtAuthenticationToken(jwtAuthenticationToken.getJwtToken(), authenticatedUser);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
