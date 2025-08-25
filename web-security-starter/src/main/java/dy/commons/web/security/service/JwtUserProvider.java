package dy.commons.web.security.service;

import dy.commons.web.security.model.JwtAuthenticationToken;
import dy.commons.web.security.model.user.AuthenticatedUser;

public interface JwtUserProvider {
    AuthenticatedUser getUser(JwtAuthenticationToken jwtAuthenticationToken);
}
