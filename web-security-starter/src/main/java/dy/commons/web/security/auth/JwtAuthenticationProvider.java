package dy.commons.web.security.auth;

import dy.commons.web.security.exception.BlockedUserException;
import dy.commons.web.security.exception.InvalidTokenException;
import dy.commons.web.security.exception.UserDeletedOrDoesNotExistException;
import dy.commons.web.security.model.JwtAuthenticationToken;
import dy.commons.web.security.model.user.User;
import dy.commons.web.security.service.JwtUserProvider;
import com.digitalyard.commons.rest.exception.exception.internal.InternalServerErrorException;
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

        try {
            User user = userProvider.getUser(jwtAuthenticationToken);
            if (user == null) {
                throw new InvalidTokenException(jwtAuthenticationToken.getJwtToken());
            }
            if (user.isAccountNonLocked()) {
                throw new BlockedUserException(user.getId());
            }
            if (user.isDeleted()) {
                throw new UserDeletedOrDoesNotExistException(user.getLogin());
            }

            return new JwtAuthenticationToken(jwtAuthenticationToken.getJwtToken(), user);
        } catch (Exception ex) {
            throw new InternalServerErrorException(ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
