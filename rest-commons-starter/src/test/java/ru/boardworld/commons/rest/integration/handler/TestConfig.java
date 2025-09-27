package ru.boardworld.commons.rest.integration.handler;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.boardworld.commons.rest.exception.exception.db.EntityNotFoundException;
import ru.boardworld.commons.rest.exception.exception.internal.InternalServerUnavailableException;

@TestConfiguration
public class TestConfig {
    public static final String NO_HANDLER_URL = "/test/no-handler";

    public static final String MISSING_PARAM_URL = "/test/missing-param";
    public static final String PARAM_NAME = "param";
    public static final String TYPE_MISMATCH_URL = "/test/type-mismatch";
    public static final String REQUIRED_TYPE = "Integer";


    public static final String ARGUMENT_NOT_VALID_URL = "/test/not-valid";
    public static final String METHOD_NOT_SUPPORTED_URL = "/test/method-not-supported";
    public static final String ENTITY_NOT_FOUND_URL = "/test/entity-not-found";
    public static final String MOCK_ENTITY_NAME = "MOCK_ENTITY_NAME";
    public static final String INTERNAL_UNAVAILABLE_URL = "/test/internal-unavailable";
    public static final String GENERIC_URL = "/test/generic";
    public static final String HTTP_MESSAGE_NOT_READABLE_URL = "/test/message-not-readable";
    public static final String MEDIA_TYPE_NOT_SUPPORTED_URL = "/test/media-type-not-supported";
    public static final String CONSTRAINT_VIOLATION_URL = "/test/constraint-violation";

    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

    public static final String PARAM_TYPE = "String";
    public static final String TEST_RESOURCE = "TestResource";
    public static final String TEST_SERVICE = "TestService";
    public static final String GENERIC_ERROR_MESSAGE = "Generic error";

    @RestController
    public static class TestController {

        @GetMapping(MISSING_PARAM_URL)
        public void throwMissingParam(@RequestParam(PARAM_NAME) String id) {
        }

        @GetMapping(TYPE_MISMATCH_URL)
        public void throwTypeMismatch(@RequestParam(PARAM_NAME) Integer id) {

        }

        @PostMapping(ARGUMENT_NOT_VALID_URL)
        public void throwMethodArgNotValid(@Valid @RequestBody TestConfig.DummyUser user) {

        }

        @PostMapping(METHOD_NOT_SUPPORTED_URL)
        public void throwMethodNotSupported() {
        }

        @GetMapping(ENTITY_NOT_FOUND_URL)
        public void throwEntityNotFound() {
            throw new EntityNotFoundException(TEST_RESOURCE, MOCK_ENTITY_NAME);
        }

        @GetMapping(INTERNAL_UNAVAILABLE_URL)
        public void throwInternalUnavailable() {
            throw new InternalServerUnavailableException(TEST_SERVICE);
        }

        @GetMapping(GENERIC_URL)
        public void throwGeneric() {
            throw new RuntimeException(GENERIC_ERROR_MESSAGE);
        }

        @PostMapping(HTTP_MESSAGE_NOT_READABLE_URL)
        public void throwMessageNotReadable(@RequestBody TestConfig.DummyUser user) {

        }

        @PostMapping(MEDIA_TYPE_NOT_SUPPORTED_URL)
        public void throwMediaTypeNotSupported(@RequestBody TestConfig.DummyUser user) {

        }
    }

    @RestController
    @Validated
    public static class ValidatedTestController {

        @PostMapping(CONSTRAINT_VIOLATION_URL)
        public void throwConstraintViolation(@Size(min = 4) @RequestParam(PARAM_NAME) String param) {
        }
    }


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldNameConstants
    public static class DummyUser {

        @NotNull(message = "Имя не может быть null")
        @Size(min = 3, max = 20, message = "Имя должно быть от 3 до 20 символов")
        private String name;

    }
}
