package ru.boardworld.commons.web.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.boardworld.commons.rest.exception.handler.ApiErrorFactory;
import ru.boardworld.commons.rest.exception.logger.ApiErrorLogger;
import ru.boardworld.commons.rest.exception.model.ApiError;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.rest.exception.model.ErrorCode;
import ru.boardworld.commons.web.security.exception.SecurityException;
import ru.boardworld.commons.web.security.model.errorCode.SecurityErrorCode;
import ru.boardworld.commons.web.security.model.errorCode.details.GenericUnauthorizedDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ApiErrorFactory apiErrorFactory;
    private final ApiErrorLogger apiErrorLogger;
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
        ApiErrorDetails details = secEx.getDetails();

        ApiError apiError = buildApiError(errorCode, details);
        apiErrorLogger.logError(apiError, secEx, request);
        writeApiErrorResponse(response, apiError);
    }

    private void handleGenericAuthenticationException(HttpServletRequest request, HttpServletResponse response,
                                                      AuthenticationException authException) throws IOException {
        GenericUnauthorizedDetails details = new GenericUnauthorizedDetails(authException.getMessage());
        ApiError apiError = buildApiError(SecurityErrorCode.UNAUTHORIZED, details);
        apiErrorLogger.logError(apiError, authException, request);
        writeApiErrorResponse(response, apiError);
    }

    private ApiError buildApiError(ErrorCode errorCode, ApiErrorDetails details) {
        return apiErrorFactory.create(errorCode, details);
    }

    private void writeApiErrorResponse(HttpServletResponse response, ApiError apiError) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(apiError.getStatus());
        String payload = objectMapper.writeValueAsString(apiError);
        response.getWriter().write(payload);
    }
}
