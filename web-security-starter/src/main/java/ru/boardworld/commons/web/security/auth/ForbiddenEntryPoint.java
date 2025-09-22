package ru.boardworld.commons.web.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.boardworld.commons.rest.exception.handler.ApiErrorFactory;
import ru.boardworld.commons.rest.exception.logger.ApiErrorLogger;
import ru.boardworld.commons.rest.exception.model.ApiError;
import ru.boardworld.commons.web.security.model.errorCode.SecurityErrorCode;
import ru.boardworld.commons.web.security.model.errorCode.details.ForbiddenDetails;
import ru.boardworld.commons.web.security.model.user.AuthenticatedUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class ForbiddenEntryPoint implements AccessDeniedHandler {
    private final ApiErrorFactory apiErrorFactory;
    private final ApiErrorLogger apiErrorLogger;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String path = request.getPathInfo();
        ForbiddenDetails details = new ForbiddenDetails(principal.getUsername(), path);

        ApiError apiError = apiErrorFactory.create(SecurityErrorCode.FORBIDDEN, details);
        apiErrorLogger.logError(apiError, accessDeniedException, request);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(SecurityErrorCode.FORBIDDEN.getHttpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}