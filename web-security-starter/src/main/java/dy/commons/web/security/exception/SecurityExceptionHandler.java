package dy.commons.web.security.exception;

import com.digitalyard.commons.rest.exception.handler.ApiErrorFactory;
import com.digitalyard.commons.rest.exception.handler.logger.ApiErrorLogger;
import com.digitalyard.commons.rest.exception.model.ApiError;
import com.digitalyard.commons.rest.exception.model.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class SecurityExceptionHandler {

    private final ApiErrorFactory apiErrorFactory;
    private final ApiErrorLogger apiErrorLogger;

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidToken(InvalidTokenException ex) {
        return handleSecurityException(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError> handleTokenExpired(TokenExpiredException ex) {
        return handleSecurityException(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BlockedUserException.class)
    public ResponseEntity<ApiError> handleBlockedUser(BlockedUserException ex) {
        return handleSecurityException(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserDeletedOrDoesNotExistException.class)
    public ResponseEntity<ApiError> handleUserDeletedOrDoesNotExist(UserDeletedOrDoesNotExistException ex) {
        return handleSecurityException(ex, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ApiError> handleSecurityException(SecurityException ex, HttpStatus status) {
        ApiError apiError = buildApiError(ex.getErrorCode(), status, ex.getDetails());
        return new ResponseEntity<>(apiError, status);
    }

    private ApiError buildApiError(ErrorCode errorCode, HttpStatus status, Map<String, Object> details) {
        ApiError apiError = apiErrorFactory.create(errorCode, status, details);
        apiErrorLogger.logError(apiError);
        return apiError;
    }
}
