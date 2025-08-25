package dy.commons.web.security.service.impl;

import dy.commons.web.security.exception.token.InvalidTokenException;
import dy.commons.web.security.model.JwtAuthenticationToken;
import dy.commons.web.security.model.user.Constants;
import dy.commons.web.security.model.user.Role;
import dy.commons.web.security.service.JwtService;
import dy.commons.web.security.utils.ClaimsUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUserProviderImplTest {
    private final JwtService jwtService = Mockito.mock(JwtService.class);
    private final JwtUserProviderImpl provider = new JwtUserProviderImpl(jwtService);

    @Test
    void getUser_success() {
        Claims claims = ClaimsUtils.createMockClaims(false, false);
        Mockito.when(jwtService.validateAccessToken("valid-token")).thenReturn(claims);

        var user = provider.getUser(new JwtAuthenticationToken("valid-token"));

        assertThat(user).isNotNull();
        assertThat(user.getId().toString()).isEqualTo(ClaimsUtils.M_ID);
        assertThat(user.getLogin()).isEqualTo(ClaimsUtils.M_LOGIN);
        assertThat(user.getFirstName()).isEqualTo(ClaimsUtils.M_FIRST_NAME);
        assertThat(user.getLastName()).isEqualTo(ClaimsUtils.M_LAST_NAME);
        assertThat(user.getRoles().stream().map(Role::getName).toList()).isEqualTo(ClaimsUtils.M_ROLES);
        assertThat(user.isAccountLocked()).isFalse();
        assertThat(user.isDeleted()).isFalse();
    }

    @Test
    void getUser_lockedUser() {
        Claims claims = ClaimsUtils.createMockClaims(true, false);
        Mockito.when(jwtService.validateAccessToken("locked")).thenReturn(claims);

        var user = provider.getUser(new JwtAuthenticationToken("locked"));

        assertThat(user.isAccountLocked()).isTrue();
        assertThat(user.isDeleted()).isFalse();
    }

    @Test
    void getUser_deletedUser() {
        Claims claims = ClaimsUtils.createMockClaims(false, true);
        Mockito.when(jwtService.validateAccessToken("deleted")).thenReturn(claims);

        var user = provider.getUser(new JwtAuthenticationToken("deleted"));

        assertThat(user.isAccountLocked()).isFalse();
        assertThat(user.isDeleted()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = Constants.class, names = {"LOGIN", "FIRST_NAME", "MIDDLE_NAME", "LAST_NAME", "USER_RIGHTS", "LOCKED", "DELETED"})
    void getUser_missingClaim_shouldThrowInvalidTokenException(Constants missingClaim) {
        Claims claims = ClaimsUtils.createMockClaims(false, false);

        claims.remove(missingClaim.name());

        Mockito.when(jwtService.validateAccessToken("token")).thenReturn(claims);

        JwtAuthenticationToken token = new JwtAuthenticationToken("token");

        assertThatThrownBy(() -> provider.getUser(token))
                .isInstanceOf(InvalidTokenException.class)
                .satisfies(ex -> {
                    InvalidTokenException ite = (InvalidTokenException) ex;
                    String reason = (String) ite.getDetails().get(InvalidTokenException.REASON);
                    assertThat(reason).contains(missingClaim.name());
                });
    }


}
