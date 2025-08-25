package com.digitalyard.commons.rest.integration.handler;

import com.digitalyard.commons.rest.exception.exception.db.EntityNotFoundException;
import com.digitalyard.commons.rest.exception.exception.internal.InternalServerUnavailableException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig{
    public static final String NO_HANDLER_URL = "/test/no-handler";
    public static final String MISSING_PARAM_URL = "/test/missing-param";
    public static final String TYPE_MISMATCH_URL = "/test/type-mismatch";
    public static final String NOT_VALID_URL = "/test/not-valid";
    public static final String METHOD_NOT_SUPPORTED_URL = "/test/method-not-supported";
    public static final String ENTITY_NOT_FOUND_URL = "/test/entity-not-found";
    public static final String INTERNAL_UNAVAILABLE_URL = "/test/internal-unavailable";
    public static final String GENERIC_URL = "/test/generic";
    public static final String HTTP_MESSAGE_NOT_READABLE_URL = "/test/message-not-readable";
    public static final String MEDIA_TYPE_NOT_SUPPORTED_URL = "/test/media-type-not-supported";
    public static final String CONSTRAINT_VIOLATION_URL = "/test/constraint-violation";

    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";
    public static final String NOT_FOUND_URL = "/not-found-url";
    public static final String PARAM_NAME = "param";
    public static final String PARAM_TYPE = "String";
    public static final String PARAM_VALUE = "abc";
    public static final String OBJECT_NAME = "object";
    public static final String TYPE_MISMATCH_MESSAGE = "Type mismatch";
    public static final String TEST_RESOURCE = "TestResource";
    public static final String TEST_SERVICE = "TestService";
    public static final String GENERIC_ERROR_MESSAGE = "Generic error";
    public static final String JSON_PARSE_MESSAGE = "JSON parse error";
    public static final String APPLICATION_XML = "application/xml";
    public static final String NAME_FIELD = "name";

    @RestController
    public static class TestController {

        // helper method used to build a valid MethodParameter instance
        public void dummyMethod(String param) {
        }

        @GetMapping(NO_HANDLER_URL)
        public void throwNoHandler() throws NoHandlerFoundException {
            throw new NoHandlerFoundException(GET_METHOD, NOT_FOUND_URL, new HttpHeaders());
        }


        @GetMapping(MISSING_PARAM_URL)
        public void throwMissingParam() throws MissingServletRequestParameterException {
            throw new MissingServletRequestParameterException(PARAM_NAME, PARAM_TYPE);
        }


        @GetMapping(TYPE_MISMATCH_URL)
        public void throwTypeMismatch() {
            try {
                MethodParameter mp = new MethodParameter(TestController.class.getMethod("dummyMethod", String.class), 0);
                throw new MethodArgumentTypeMismatchException(PARAM_VALUE, Integer.class, PARAM_NAME, mp, new IllegalArgumentException(TYPE_MISMATCH_MESSAGE));
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }


        @GetMapping(NOT_VALID_URL)
        public void throwMethodArgNotValid() throws MethodArgumentNotValidException {
            try {
                MethodParameter mp = new MethodParameter(TestController.class.getMethod("dummyMethod", String.class), 0);
                throw new MethodArgumentNotValidException(mp, new BindException(new Object(), OBJECT_NAME));
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException(ex);
            }
        }


        @GetMapping(METHOD_NOT_SUPPORTED_URL)
        public void throwMethodNotSupported() throws HttpRequestMethodNotSupportedException {
            throw new HttpRequestMethodNotSupportedException(GET_METHOD, List.of(POST_METHOD));
        }


        @GetMapping(ENTITY_NOT_FOUND_URL)
        public void throwEntityNotFound() {
            throw new EntityNotFoundException(TEST_RESOURCE, UUID.randomUUID());
        }


        @GetMapping(INTERNAL_UNAVAILABLE_URL)
        public void throwInternalUnavailable() {
            throw new InternalServerUnavailableException(TEST_SERVICE);
        }


        @GetMapping(GENERIC_URL)
        public void throwGeneric() {
            throw new RuntimeException(GENERIC_ERROR_MESSAGE);
        }

        @GetMapping(HTTP_MESSAGE_NOT_READABLE_URL)
        public void throwMessageNotReadable() {
            throw new HttpMessageNotReadableException(JSON_PARSE_MESSAGE, new RuntimeException("Root cause"), mock(HttpInputMessage.class));
        }

        @GetMapping(MEDIA_TYPE_NOT_SUPPORTED_URL)
        public void throwMediaTypeNotSupported() throws HttpMediaTypeNotSupportedException {
            throw new HttpMediaTypeNotSupportedException(
                    MediaType.valueOf(APPLICATION_XML),
                    List.of(MediaType.APPLICATION_JSON)
            );
        }

        @GetMapping(CONSTRAINT_VIOLATION_URL)
        public void throwConstraintViolation() {
            try (var factory = Validation.buildDefaultValidatorFactory()) {
                Validator validator = factory.getValidator();
                DummyForValidation dummy = new DummyForValidation();
                Set<ConstraintViolation<DummyForValidation>> violations = validator.validate(dummy);
                throw new ConstraintViolationException(violations);
            }
        }

        public static class DummyForValidation {
            @NotNull
            private String name;
        }
    }
}
