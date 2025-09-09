package ru.boardworld.commons.web.security.service.impl;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.web.security.auth.JwtAuthenticationProvider;
import ru.boardworld.commons.web.security.exception.token.InvalidTokenException;
import ru.boardworld.commons.web.security.model.JwtAuthenticationToken;
import ru.boardworld.commons.web.security.model.errorCode.details.InvalidTokenDetails;
import ru.boardworld.commons.web.security.model.user.AuthenticatedUser;
import ru.boardworld.commons.web.security.model.user.Constants;
import ru.boardworld.commons.web.security.model.user.Role;
import ru.boardworld.commons.web.security.service.JwtValidator;
import ru.boardworld.commons.web.security.utils.ClaimsUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtAuthenticationProviderTest {
    private final JwtValidator jwtValidator = Mockito.mock(JwtValidator.class);
    private final JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtValidator);

    @Test
    void getUser_success() {
        Claims claims = ClaimsUtils.createMockClaims();
        Mockito.when(jwtValidator.validateToken("valid-token")).thenReturn(claims);

        Authentication jwtAuthToken = provider.authenticate(new JwtAuthenticationToken("valid-token"));
        Assertions.assertInstanceOf(AuthenticatedUser.class, jwtAuthToken.getPrincipal());
        AuthenticatedUser authenticatedUser = (AuthenticatedUser) jwtAuthToken.getPrincipal();

        assertThat(authenticatedUser).isNotNull();
        assertThat(authenticatedUser.getId().toString()).isEqualTo(ClaimsUtils.M_ID);
        assertThat(authenticatedUser.getUsername()).isEqualTo(ClaimsUtils.M_USERNAME);
        assertThat(authenticatedUser.getRoles().stream().map(Role::getName).toList()).isEqualTo(ClaimsUtils.M_ROLES);
    }

    @ParameterizedTest
    @EnumSource(value = Constants.class, names = {"USERNAME", "USER_RIGHTS"})
    void getUser_missingClaim_shouldThrowInvalidTokenException(Constants missingClaim) {
        Claims claims = ClaimsUtils.createMockClaims();

        claims.remove(missingClaim.name());

        Mockito.when(jwtValidator.validateToken("token")).thenReturn(claims);

        JwtAuthenticationToken token = new JwtAuthenticationToken("token");

        assertThatThrownBy(() -> provider.authenticate(token))
                .isInstanceOf(InvalidTokenException.class)
                .satisfies(ex -> {
                    InvalidTokenException ite = (InvalidTokenException) ex;
                    ApiErrorDetails details = ite.getDetails();
                    Assertions.assertInstanceOf(InvalidTokenDetails.class, details);
                    InvalidTokenDetails itd = (InvalidTokenDetails) details;
                    String reason = itd.getReason();
                    assertThat(reason).contains(missingClaim.name());
                });
    }


}
