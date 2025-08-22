package dy.commons.web.security.service;

import dy.commons.web.security.model.JwtAuthenticationToken;
import dy.commons.web.security.model.user.User;

public interface JwtUserProvider {
    User getUser(JwtAuthenticationToken jwtAuthenticationToken);
}
