package com.digitalyard.commons.rest.exception.handler;

import com.digitalyard.commons.rest.exception.exception.AbstractApiException;
import com.digitalyard.commons.rest.exception.exception.db.EntityNotFoundException;
import com.digitalyard.commons.rest.exception.exception.internal.InternalServerErrorException;
import com.digitalyard.commons.rest.exception.logger.ApiErrorLogger;
import com.digitalyard.commons.rest.exception.mapper.ExceptionDetailsMapperRegistry;
import com.digitalyard.commons.rest.exception.model.ApiError;
import com.digitalyard.commons.rest.exception.model.CommonErrorCode;
import com.digitalyard.commons.rest.exception.model.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Collections;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiErrorFactory apiErrorFactory;
    private final ApiErrorLogger apiErrorLogger;
    private final ExceptionDetailsMapperRegistry detailsMapperRegistry;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        return handleApiException(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiError> handleInternalServerErrorException(InternalServerErrorException ex, HttpServletRequest request) {
        return handleApiException(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ApiError> handleApiException(AbstractApiException ex, HttpStatus status, HttpServletRequest request) {
        ApiError apiError = buildApiError(ex.getErrorCode(), status, ex.getDetails());
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, status);
    }

    /**
     * Обработка отсутствия ресурсов
     * @see com.digitalyard.commons.rest.exception.mapper.NoHandlerFoundDetailsMapper
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        Map<String, Object> details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    /**
     * --- 400: Missing request parameter ---
     * @see com.digitalyard.commons.rest.exception.mapper.MissingRequestParameterDetailsMapper
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        Map<String, Object> details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.REQUIRED_PARAMETER_MISSING, HttpStatus.BAD_REQUEST, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * --- 400: Argument type mismatch (e.g. id=abc where id is UUID/Long) ---
     * @see com.digitalyard.commons.rest.exception.mapper.MethodArgumentTypeMismatchDetailsMapper
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        Map<String, Object> details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.INVALID_PARAMETER_VALUE, HttpStatus.BAD_REQUEST, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    /**
     * --- 400: HTTP message not readable (invalid JSON) ---
     * @see com.digitalyard.commons.rest.exception.mapper.HttpMessageNotReadableDetailsMapper
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Map<String, Object> details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.HTTP_MESSAGE_NOT_READABLE, HttpStatus.BAD_REQUEST, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    /**
     * --- 400: Validation exceptions from method-level @Validated ---
     * @see com.digitalyard.commons.rest.exception.mapper.ConstraintViolationDetailsMapper
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, Object> details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * --- 400: Обработка исключений валидации Spring ---
     * @see com.digitalyard.commons.rest.exception.mapper.MethodArgumentNotValidDetailsMapper
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * --- 405: Method not supported ---
     * @see com.digitalyard.commons.rest.exception.mapper.HttpRequestMethodNotSupportedDetailsMapper
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        Map<String, Object> details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.METHOD_NOT_ALLOWED, HttpStatus.METHOD_NOT_ALLOWED, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * --- 415: Unsupported media type ---
     * @see com.digitalyard.commons.rest.exception.mapper.HttpMediaTypeNotSupportedDetailsMapper
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        Map<String, Object> details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE, HttpStatus.UNSUPPORTED_MEDIA_TYPE, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * --- 500: Unknown exception ---
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaught(Exception ex, HttpServletRequest request) {
        ApiError apiError = buildApiError(CommonErrorCode.UNKNOWN_INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiError buildApiError(ErrorCode errorCode, HttpStatus status) {
        return buildApiError(errorCode, status, Collections.emptyMap());
    }

    private ApiError buildApiError(ErrorCode errorCode, HttpStatus status, Map<String, Object> details) {
        return apiErrorFactory.create(errorCode, status, details);
    }

    private void logException(ApiError apiError, Exception exception, HttpServletRequest request) {
        apiErrorLogger.logError(apiError, exception, request);
    }
}