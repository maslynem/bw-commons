package dy.digitalyard.commons.web.security.service;

import dy.digitalyard.commons.web.security.model.JwtAuthenticationToken;
import dy.digitalyard.commons.web.security.model.user.AuthenticatedUser;

public interface JwtUserProvider {
    AuthenticatedUser getUser(JwtAuthenticationToken jwtAuthenticationToken);
}
