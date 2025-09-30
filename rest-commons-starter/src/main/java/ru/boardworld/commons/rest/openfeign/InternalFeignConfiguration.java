package ru.boardworld.commons.rest.openfeign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class InternalFeignConfiguration {

    @Bean
    public ErrorDecoder feignApiErrorDecoder(ObjectMapper objectMapper) {
        return new InternalFeignApiErrorDecoder(objectMapper);
    }
}