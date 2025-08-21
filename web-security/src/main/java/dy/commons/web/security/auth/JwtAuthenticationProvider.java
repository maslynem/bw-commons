package dy.commons.web.security.auth;

import dy.commons.web.security.model.JwtAuthenticationToken;
import dy.commons.web.security.model.user.User;
import dy.commons.web.security.service.JwtUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
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
                throw new BadCredentialsException("ACCESS_DENIED"); //TODO custom
            }
            if (user.isAccountNonLocked()) {
                throw new LockedException("BLOCKED_USER"); //TODO custom
            }
            if (user.isDeleted()) {
                throw new LockedException("USER_DELETED_OR_DOES_NOT_EXIST"); //TODO custom
            }

//            this.userAccessTimestampService.userAccessed(user);
            return new JwtAuthenticationToken(jwtAuthenticationToken.getJwtToken(), user);
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException("INTERNAL_SERVER_ERROR", ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
