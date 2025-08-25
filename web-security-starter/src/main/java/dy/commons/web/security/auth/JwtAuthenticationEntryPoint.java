package dy.commons.web.security.auth;

import com.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import com.digitalyard.commons.rest.exception.model.ApiError;
import com.digitalyard.commons.rest.exception.model.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dy.commons.web.security.exception.SecurityException;
import dy.commons.web.security.model.errorCode.SecurityErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ApiErrorFactory apiErrorFactory;
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (authException instanceof SecurityException) {
            handleSecurityException(request, response, (SecurityException) authException);
        } else {
            handleGenericAuthenticationException(request, response, authException);
        }
    }

    private void handleSecurityException(HttpServletRequest request, HttpServletResponse response,
                                         SecurityException secEx) throws IOException {
        ErrorCode errorCode = secEx.getErrorCode();
        Map<String, Object> details = secEx.getDetails();

        HttpStatus status = mapStatusForErrorCode(errorCode);
        ApiError apiError = buildApiError(errorCode, status, details);
        writeApiErrorResponse(response, status, apiError);
    }

    private void handleGenericAuthenticationException(HttpServletRequest request, HttpServletResponse response,
                                                      AuthenticationException authException) throws IOException {
        Map<String, Object> details = Collections.singletonMap(
                SecurityErrorCode.UNAUTHORIZED.name(),
                authException.getMessage()
        );
        ApiError apiError = buildApiError(SecurityErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, details);
        writeApiErrorResponse(response, HttpStatus.UNAUTHORIZED, apiError);
    }

    private HttpStatus mapStatusForErrorCode(ErrorCode errorCode) {
        if (errorCode == SecurityErrorCode.BLOCKED_USER) {
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.UNAUTHORIZED;
    }

    private ApiError buildApiError(ErrorCode errorCode, HttpStatus status, Map<String, Object> details) {
        return apiErrorFactory.create(errorCode, status, details);
    }

    private void writeApiErrorResponse(HttpServletResponse response, HttpStatus status, ApiError apiError) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        String payload = objectMapper.writeValueAsString(apiError);
        response.getWriter().write(payload);
    }
}
