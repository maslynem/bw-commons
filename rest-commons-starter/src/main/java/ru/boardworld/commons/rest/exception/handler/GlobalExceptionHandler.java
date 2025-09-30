package ru.boardworld.commons.rest.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.boardworld.commons.rest.exception.exception.AbstractApiException;
import ru.boardworld.commons.rest.exception.logger.ApiErrorLogger;
import ru.boardworld.commons.rest.exception.mapper.ExceptionDetailsMapperRegistry;
import ru.boardworld.commons.rest.exception.mapper.impl.*;
import ru.boardworld.commons.rest.exception.model.ApiError;
import ru.boardworld.commons.rest.exception.model.ApiErrorDetails;
import ru.boardworld.commons.rest.exception.model.CommonErrorCode;
import ru.boardworld.commons.rest.exception.model.ErrorCode;
import ru.boardworld.commons.rest.exception.model.details.EmptyDetails;
import ru.boardworld.commons.rest.openfeign.InternalFeignException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiErrorFactory apiErrorFactory;
    private final ApiErrorLogger apiErrorLogger;
    private final ExceptionDetailsMapperRegistry detailsMapperRegistry;

    @ExceptionHandler(InternalFeignException.class) // Ловим наше новое исключение
    public ResponseEntity<ApiError> handleServiceApi(InternalFeignException ex, HttpServletRequest request) {
        ApiError apiError = ex.getApiError();
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, apiError.getCode().getHttpStatus());
    }

    @ExceptionHandler(AbstractApiException.class)
    private ResponseEntity<ApiError> handleApiException(AbstractApiException ex, HttpServletRequest request) {
        ApiError apiError = buildApiError(ex.getErrorCode(), ex.getDetails());
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, apiError.getCode().getHttpStatus());
    }

    /**
     * Обработка отсутствия ресурсов
     *
     * @see NoHandlerFoundDetailsMapper
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        ApiErrorDetails details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.NOT_FOUND, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, CommonErrorCode.NOT_FOUND.getHttpStatus());
    }

    /**
     * --- 400: Missing request parameter ---
     *
     * @see MissingRequestParameterDetailsMapper
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest request) {
        ApiErrorDetails details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.REQUIRED_PARAMETER_MISSING, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, CommonErrorCode.REQUIRED_PARAMETER_MISSING.getHttpStatus());
    }

    /**
     * --- 400: Argument type mismatch (e.g. id=abc where id is UUID/Long) ---
     *
     * @see MethodArgumentTypeMismatchDetailsMapper
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ApiErrorDetails details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.ARGUMENT_TYPE_MISMATCH, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, CommonErrorCode.ARGUMENT_TYPE_MISMATCH.getHttpStatus());
    }

    /**
     * --- 400: HTTP message not readable (invalid JSON) ---
     *
     * @see HttpMessageNotReadableDetailsMapper
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ApiErrorDetails details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.HTTP_MESSAGE_NOT_READABLE, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, CommonErrorCode.HTTP_MESSAGE_NOT_READABLE.getHttpStatus());
    }


    /**
     * --- 400: Validation exceptions from method-level @Validated ---
     *
     * @see ConstraintViolationDetailsMapper
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        ApiErrorDetails details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.VALIDATION_FAILED, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, CommonErrorCode.VALIDATION_FAILED.getHttpStatus());
    }

    /**
     * --- 400: Обработка исключений валидации Spring ---
     *
     * @see MethodArgumentNotValidDetailsMapper
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ApiErrorDetails details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.VALIDATION_FAILED, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, CommonErrorCode.VALIDATION_FAILED.getHttpStatus());
    }


    /**
     * --- 405: Method not supported ---
     *
     * @see HttpRequestMethodNotSupportedDetailsMapper
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        ApiErrorDetails details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.METHOD_NOT_SUPPORTED, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, CommonErrorCode.METHOD_NOT_SUPPORTED.getHttpStatus());
    }

    /**
     * --- 415: Unsupported media type ---
     *
     * @see HttpMediaTypeNotSupportedDetailsMapper
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        ApiErrorDetails details = detailsMapperRegistry.map(ex);
        ApiError apiError = buildApiError(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE, details);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, CommonErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpStatus());
    }

    /**
     * --- 500: Unknown exception ---
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaught(Exception ex, HttpServletRequest request) {
        ApiError apiError = buildApiError(CommonErrorCode.UNKNOWN_INTERNAL_ERROR, EmptyDetails.INSTANCE);
        logException(apiError, ex, request);
        return new ResponseEntity<>(apiError, CommonErrorCode.UNKNOWN_INTERNAL_ERROR.getHttpStatus());
    }

    private ApiError buildApiError(ErrorCode errorCode, ApiErrorDetails details) {
        return apiErrorFactory.create(errorCode, details);
    }

    private void logException(ApiError apiError, Exception exception, HttpServletRequest request) {
        apiErrorLogger.logError(apiError, exception, request);
    }
}