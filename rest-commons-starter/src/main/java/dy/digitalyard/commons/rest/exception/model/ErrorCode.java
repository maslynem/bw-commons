package dy.digitalyard.commons.rest.exception.model;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();

    default String getCode() {
        return name();
    }

    HttpStatus getHttpStatus();

     Class<? extends ApiErrorDetails> getDetailsClass();
}
