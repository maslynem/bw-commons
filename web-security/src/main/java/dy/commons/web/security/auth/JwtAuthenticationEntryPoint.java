package dy.commons.web.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.model.ApiError;
import exception.model.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ApiError apiError = ApiError.builder()
                .message(ErrorCode.AUTHENTICATION_ERROR.name())
                .exception(authException)
                .errors(Collections.singletonMap(
                        ErrorCode.AUTHENTICATION_ERROR.name(),
                        authException.getMessage()
                ))
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}