package dy.commons.web.security.auth;

import dy.commons.web.security.model.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationManager authenticationManager;
    private final String jwtHeader;
    private final String bearerPrefix;

    public JwtAuthenticationTokenFilter(AuthenticationManager authenticationManager,
                                        AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtHeader = "Authorization";
        this.bearerPrefix = "Bearer ";
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(jwtHeader);

        log.debug("Read auth header {}: {}", jwtHeader, authHeader);

        if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, bearerPrefix)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String authenticationToken = authHeader.substring(bearerPrefix.length());
            log.debug("JWT was extracted: {}", authenticationToken);
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(authenticationToken);
            Authentication authResult = authenticationManager.authenticate(jwtAuthenticationToken);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authResult);
            SecurityContextHolder.setContext(context);

            onSuccessfulAuthentication(request, response, authResult);
        } catch (AuthenticationException failed) {
            log.debug("Failed authentication: ", failed);
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, failed);
            return;
        }

        chain.doFilter(request, response);
    }

    private void onSuccessfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            Authentication authResult) {
        if (log.isDebugEnabled()) {
            final String username = Optional.of(authResult)
                    .map(Authentication::getPrincipal)
                    .filter(principal -> principal instanceof UserDetails)
                    .map(UserDetails.class::cast)
                    .map(UserDetails::getUsername)
                    .orElse("Unknown");
            log.debug("Successfully Authenticated <{}>", username);
        }
    }

}