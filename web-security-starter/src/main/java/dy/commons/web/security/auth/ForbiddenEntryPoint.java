package dy.commons.web.security.auth;

import com.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import com.digitalyard.commons.rest.exception.model.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import dy.commons.web.security.model.errorCode.SecurityErrorCode;
import dy.commons.web.security.model.user.AuthenticatedUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class ForbiddenEntryPoint implements AccessDeniedHandler {
    public static final String USER_LOGIN = "LOGIN";
    public static final String URL = "URL";

    private final ApiErrorFactory apiErrorFactory;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        AuthenticatedUser principal = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String path = request.getPathInfo();
        Map<String, Object> details = Map.of(USER_LOGIN, principal.getLogin(),
                URL, path);
        ApiError apiError = apiErrorFactory.create(SecurityErrorCode.FORBIDDEN, HttpStatus.FORBIDDEN, details);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}