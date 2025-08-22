package dy.commons.web.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import com.digitalyard.commons.rest.exception.handler.logger.ApiErrorLogger;
import com.digitalyard.commons.rest.exception.model.ApiError;
import com.digitalyard.commons.rest.exception.model.CommonErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ApiErrorFactory apiErrorFactory;
    private final ApiErrorLogger apiErrorLogger;
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        Map<String, Object> details = Collections.singletonMap(
                CommonErrorCode.UNAUTHORIZED.name(),
                authException.getMessage()
        );

        ApiError apiError = apiErrorFactory.create(CommonErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, details);
        apiErrorLogger.logError(apiError);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}