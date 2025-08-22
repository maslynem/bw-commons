package dy.commons.web.security.service.impl;

import dy.commons.web.security.model.JwtAuthenticationToken;
import dy.commons.web.security.model.user.Constants;
import dy.commons.web.security.model.user.User;
import dy.commons.web.security.service.JwtService;
import dy.commons.web.security.service.JwtUserProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtUserProviderImpl implements JwtUserProvider {
    private final JwtService jwtService;

    @Override
    public User getUser(JwtAuthenticationToken jwtAuthenticationToken) {
        log.debug("Validating JWT: {}", jwtAuthenticationToken.getJwtToken());

        Claims claims = jwtService.validateAccessToken(jwtAuthenticationToken.getJwtToken());

        log.debug("JWT claims were extracted: {}", claims);

        String login = claims.get(Constants.LOGIN.name(), String.class);

        log.debug("Username extracted from JWT: {}", login);

        if (!StringUtils.hasText(login)) {
            log.debug("Username is empty");
            throw new RuntimeException(login); // TODO throw UsernameNotFoundException
        }

        return User.builder()
                .id(UUID.fromString(claims.getSubject()))
                .login(login)
                .firstName(claims.get(Constants.FIRST_NAME.name(), String.class))
                .middleName(claims.get(Constants.MIDDLE_NAME.name(), String.class))
                .lastName(claims.get(Constants.LAST_NAME.name(), String.class))
                .roles(claims.get(Constants.USER_RIGHTS.name(), Set.class))
                .build();
    }
}
