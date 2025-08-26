package dy.digitalyard.commons.rest.integration.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import dy.digitalyard.commons.rest.exception.config.ApiErrorConfiguration;
import dy.digitalyard.commons.rest.exception.config.ExceptionHandlerConfiguration;
import dy.digitalyard.commons.rest.exception.model.ApiError;
import dy.digitalyard.commons.rest.exception.model.CommonErrorCode;
import dy.digitalyard.commons.rest.exception.model.details.*;
import dy.digitalyard.commons.rest.jackson.ObjectMapperConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        classes = {
                TestConfig.class,
                ApiErrorConfiguration.class,
                ObjectMapperConfiguration.class,
                ExceptionHandlerConfiguration.class
        },
        properties = {
                "spring.mvc.throw-exception-if-no-handler-found=true",
                "spring.web.resources.add-mappings=false"
        }
)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
@Tag("integration")
class GlobalExceptionHandlerTest {
    private final String CODE = ApiError.Fields.code;
    private final String DETAILS = ApiError.Fields.details;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void noHandlerFound_returnsNotFound_andDetailsContainMethodAndUrl() throws Exception {
        mockMvc.perform(get(TestConfig.NO_HANDLER_URL))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.NOT_FOUND.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, NoHandlerFoundDetails.Fields.method)).value(TestConfig.GET_METHOD))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, NoHandlerFoundDetails.Fields.url)).value(TestConfig.NO_HANDLER_URL));
    } 

    @Test
    void missingServletRequestParameter_returnsBadRequest_andDetailsContainParameterInfo() throws Exception {
        mockMvc.perform(get(TestConfig.MISSING_PARAM_URL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.REQUIRED_PARAMETER_MISSING.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, MissingRequestParameterDetails.Fields.parameterName)).value(TestConfig.PARAM_NAME))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, MissingRequestParameterDetails.Fields.parameterType)).value(TestConfig.PARAM_TYPE));
    } 

    @Test
    void methodArgumentTypeMismatch_returnsBadRequest_andDetailsContainParameterName() throws Exception {
        mockMvc.perform(get(TestConfig.TYPE_MISMATCH_URL).param(TestConfig.PARAM_NAME, "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.ARGUMENT_TYPE_MISMATCH.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, ArgumentTypeMismatchDetails.Fields.parameterName)).value(TestConfig.PARAM_NAME))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, ArgumentTypeMismatchDetails.Fields.requiredType)).value(TestConfig.REQUIRED_TYPE));
    }

    @Test
    void methodArgumentNotValid_returnsBadRequest_andDetailsContainViolationsKey() throws Exception {
        TestConfig.DummyUser user = new TestConfig.DummyUser("a");
        mockMvc.perform(post(TestConfig.ARGUMENT_NOT_VALID_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.VALIDATION_FAILED.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, ValidationFailedDetails.Fields.violations)).exists())
                .andExpect(jsonPath(format("$.%s.%s.%s", DETAILS, ValidationFailedDetails.Fields.violations, TestConfig.DummyUser.Fields.name)).exists());
    }

    @Test
    void constraintViolation_returnsBadRequest_andDetailsContainViolations() throws Exception {
        mockMvc.perform(post(TestConfig.CONSTRAINT_VIOLATION_URL).param(TestConfig.PARAM_NAME, ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.VALIDATION_FAILED.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, ValidationFailedDetails.Fields.violations)).exists())
                .andExpect(jsonPath(format("$.%s.%s.%s", DETAILS, ValidationFailedDetails.Fields.violations, TestConfig.PARAM_NAME)).exists());
    }

    @Test
    void httpRequestMethodNotSupported_returnsMethodNotAllowed_andDetailsContainSupportedMethods() throws Exception {
        mockMvc.perform(get(TestConfig.METHOD_NOT_SUPPORTED_URL))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.METHOD_NOT_SUPPORTED.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, MethodNotSupportedDetails.Fields.supportedMethods)).exists())
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, MethodNotSupportedDetails.Fields.supportedMethods)).value(hasItem(TestConfig.POST_METHOD)));
    }

    @Test
    void httpMessageNotReadable_returnsBadRequest_andDetailsContainReason() throws Exception {
        String userJson = """
                {
                  "username": "Alice"
                }
                """;
        mockMvc.perform(post(TestConfig.HTTP_MESSAGE_NOT_READABLE_URL)
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.HTTP_MESSAGE_NOT_READABLE.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, HttpMessageNotReadableDetails.Fields.reason)).exists());
    }

    @Test
    void httpMediaTypeNotSupported_returnsUnsupportedMediaType_andDetailsContainContentType() throws Exception {
        String plainText = "\"name=Alise\"";
        mockMvc.perform(post(TestConfig.MEDIA_TYPE_NOT_SUPPORTED_URL)
                .contentType("text/plain")  // <-- неверный Content-Type
                .content(plainText))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, UnsupportedMediaTypeDetails.Fields.contentType)).exists())
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, UnsupportedMediaTypeDetails.Fields.supportedMediaTypes)).exists());
    }


    @Test
    void entityNotFound_returnsNotFound_andDetailsContainResourceTypeAndId() throws Exception {
        mockMvc.perform(get(TestConfig.ENTITY_NOT_FOUND_URL))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.ENTITY_NOT_FOUND.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, EntityNotFoundDetails.Fields.resourceType)).value(TestConfig.TEST_RESOURCE))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, EntityNotFoundDetails.Fields.id)).isNotEmpty());
    }

    @Test
    void internalServerUnavailable_returnsInternalServerError_andDetailsContainServiceName() throws Exception {
        mockMvc.perform(get(TestConfig.INTERNAL_UNAVAILABLE_URL))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.SERVICE_UNAVAILABLE.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, ServiceUnavailableDetails.Fields.serviceName)).value(TestConfig.TEST_SERVICE));
    }

    @Test
    void genericException_returnsInternalServerError_andUnknownInternalCode() throws Exception {
        mockMvc.perform(get(TestConfig.GENERIC_URL))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.UNKNOWN_INTERNAL_ERROR.name()))
                .andExpect(jsonPath(format("$.%s", DETAILS)).value(anEmptyMap()));
    }
}
