package com.digitalyard.commons.rest.integration.handler;

import com.digitalyard.commons.rest.exception.config.ApiErrorConfiguration;
import com.digitalyard.commons.rest.exception.config.ExceptionHandlerConfiguration;
import com.digitalyard.commons.rest.exception.exception.db.EntityNotFoundException;
import com.digitalyard.commons.rest.exception.exception.internal.InternalServerUnavailableException;
import com.digitalyard.commons.rest.exception.mapper.*;
import com.digitalyard.commons.rest.exception.model.ApiError;
import com.digitalyard.commons.rest.exception.model.CommonErrorCode;
import com.digitalyard.commons.rest.jackson.ObjectMapperConfiguration;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = {
        TestConfig.class,
        ApiErrorConfiguration.class,
        ObjectMapperConfiguration.class,
        ExceptionHandlerConfiguration.class
})
@AutoConfigureMockMvc
@AutoConfigureWebMvc
@Tag("integration")
class GlobalExceptionHandlerTest {
    private final String CODE = ApiError.Fields.code;
    private final String DETAILS = ApiError.Fields.details;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void noHandlerFound_returnsNotFound_andDetailsContainMethodAndUrl() throws Exception {
        mockMvc.perform(get(TestConfig.NO_HANDLER_URL))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.NOT_FOUND.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, NoHandlerFoundDetailsMapper.METHOD)).value(TestConfig.GET_METHOD))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, NoHandlerFoundDetailsMapper.URL)).value(TestConfig.NOT_FOUND_URL));
    }

    @Test
    void missingServletRequestParameter_returnsBadRequest_andDetailsContainParameterInfo() throws Exception {
        mockMvc.perform(get(TestConfig.MISSING_PARAM_URL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.REQUIRED_PARAMETER_MISSING.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, MissingRequestParameterDetailsMapper.PARAMETER_NAME)).value(TestConfig.PARAM_NAME))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, MissingRequestParameterDetailsMapper.PARAMETER_TYPE)).value(TestConfig.PARAM_TYPE));
    }

    @Test
    void methodArgumentTypeMismatch_returnsBadRequest_andDetailsContainParameterName() throws Exception {
        mockMvc.perform(get(TestConfig.TYPE_MISMATCH_URL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.INVALID_PARAMETER_VALUE.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, MethodArgumentTypeMismatchDetailsMapper.PARAMETER_NAME)).value(TestConfig.PARAM_NAME));
    }

    @Test
    void methodArgumentNotValid_returnsBadRequest_andDetailsContainViolationsKey() throws Exception {
        mockMvc.perform(get(TestConfig.NOT_VALID_URL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.VALIDATION_FAILED.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, MethodArgumentNotValidDetailsMapper.VIOLATIONS)).exists());
    }

    @Test
    void httpRequestMethodNotSupported_returnsMethodNotAllowed_andDetailsContainSupportedMethods() throws Exception {
        mockMvc.perform(get(TestConfig.METHOD_NOT_SUPPORTED_URL))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.METHOD_NOT_ALLOWED.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, HttpRequestMethodNotSupportedDetailsMapper.SUPPORTED_METHODS)).exists());
    }

    @Test
    void httpMessageNotReadable_returnsBadRequest_andDetailsContainReason() throws Exception {
        mockMvc.perform(get(TestConfig.HTTP_MESSAGE_NOT_READABLE_URL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.HTTP_MESSAGE_NOT_READABLE.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, HttpMessageNotReadableDetailsMapper.REASON)).value(TestConfig.JSON_PARSE_MESSAGE));
    }


    @Test
    void httpMediaTypeNotSupported_returnsUnsupportedMediaType_andDetailsContainContentType() throws Exception {
        mockMvc.perform(get(TestConfig.MEDIA_TYPE_NOT_SUPPORTED_URL))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.UNSUPPORTED_MEDIA_TYPE.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, HttpMediaTypeNotSupportedDetailsMapper.CONTENT_TYPE)).value(TestConfig.APPLICATION_XML))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, HttpMediaTypeNotSupportedDetailsMapper.SUPPORTED)).exists());
    }


    @Test
    void constraintViolation_returnsBadRequest_andDetailsContainViolations() throws Exception {
        mockMvc.perform(get(TestConfig.CONSTRAINT_VIOLATION_URL))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.VALIDATION_FAILED.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, ConstraintViolationDetailsMapper.VIOLATIONS)).exists());
    }

    @Test
    void entityNotFound_returnsNotFound_andDetailsContainResourceTypeAndId() throws Exception {
        mockMvc.perform(get(TestConfig.ENTITY_NOT_FOUND_URL))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.ENTITY_NOT_FOUND.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, EntityNotFoundException.RESOURCE_TYPE)).value(TestConfig.TEST_RESOURCE))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, EntityNotFoundException.ID)).isNotEmpty());
    }

    @Test
    void internalServerUnavailable_returnsInternalServerError_andDetailsContainServiceName() throws Exception {
        mockMvc.perform(get(TestConfig.INTERNAL_UNAVAILABLE_URL))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.SERVICE_UNAVAILABLE.name()))
                .andExpect(jsonPath(format("$.%s.%s", DETAILS, InternalServerUnavailableException.SERVICE_NAME)).value(TestConfig.TEST_SERVICE));
    }

    @Test
    void genericException_returnsInternalServerError_andUnknownInternalCode() throws Exception {
        mockMvc.perform(get(TestConfig.GENERIC_URL))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath(format("$.%s", CODE)).value(CommonErrorCode.UNKNOWN_INTERNAL_ERROR.name()));
    }
}
