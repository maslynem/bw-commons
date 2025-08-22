package com.digitalyard.commons.rest.exception.handler;

import com.digitalyard.commons.rest.exception.exception.AbstractApiException;
import com.digitalyard.commons.rest.exception.exception.db.NotFoundException;
import com.digitalyard.commons.rest.exception.exception.internal.InternalServerErrorException;
import com.digitalyard.commons.rest.exception.exception.validation.ValidationException;
import com.digitalyard.commons.rest.exception.handler.logger.ApiErrorLogger;
import com.digitalyard.commons.rest.exception.model.ApiError;
import com.digitalyard.commons.rest.exception.model.CommonErrorCode;
import com.digitalyard.commons.rest.exception.model.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiErrorFactory apiErrorFactory;
    private final ApiErrorLogger apiErrorLogger;


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex) {
        return handleApiException(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiError> handleInternalServerErrorException(InternalServerErrorException ex) {
        return handleApiException(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException ex) {
        return handleApiException(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ApiError> handleApiException(AbstractApiException ex, HttpStatus status) {
        ApiError apiError = buildApiError(ex.getErrorCode(), status, ex.getDetails());
        return new ResponseEntity<>(apiError, status);
    }

    // Обработка исключений валидации Spring
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> grouped = new LinkedHashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(err -> grouped
                        .computeIfAbsent(err.getField(), key -> new ArrayList<>())
                        .add(err.getDefaultMessage() != null ? err.getDefaultMessage() : "Invalid value"));

        List<String> global = ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(e -> e.getDefaultMessage() != null ? e.getDefaultMessage() : "Validation error")
                .collect(Collectors.toList());
        if (!global.isEmpty()) {
            grouped.put("_global", global);
        }
        Map<String, Object> details = new LinkedHashMap<>(grouped);
        ApiError apiError = buildApiError(CommonErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, details);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Обработка отсутствия ресурсов
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFound(NoHandlerFoundException ex) {
        Map<String, Object> details = Map.of(
                "method", ex.getHttpMethod(),
                "url", ex.getRequestURL()
        );
        ApiError apiError = buildApiError(CommonErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, details);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaught(Exception ex) {
        log.error("", ex);
        ApiError apiError = buildApiError(CommonErrorCode.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiError buildApiError(ErrorCode errorCode, HttpStatus status) {
        return buildApiError(errorCode, status, Collections.emptyMap());
    }

    private ApiError buildApiError(ErrorCode errorCode, HttpStatus status, Map<String, Object> details) {
        ApiError apiError = apiErrorFactory.create(errorCode, status, details);
        apiErrorLogger.logError(apiError);
        return apiError;
    }


}