package dy.commons.web.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.model.ApiError;
import exception.model.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class ForbiddenEntryPoint implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        ApiError apiError = ApiError.builder()
                .message(ErrorCode.ACCESS_DENIED.name())
                .exception(accessDeniedException)
                .errors(Collections.singletonMap(
                        ErrorCode.ACCESS_DENIED.name(),
                        accessDeniedException.getMessage()
                ))
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}