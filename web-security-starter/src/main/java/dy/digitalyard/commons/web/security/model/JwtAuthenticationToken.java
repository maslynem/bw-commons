package dy.digitalyard.commons.web.security.model;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String jwtToken;
    private final UserDetails principal;

    public JwtAuthenticationToken(String jwtToken) {
        super(Collections.emptyList());
        this.jwtToken = jwtToken;
        principal = null;
    }

    public JwtAuthenticationToken(String jwtToken, UserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.jwtToken = jwtToken;
        this.principal = userDetails;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

}
