package exception.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ApiError {
    private String message;
    private Exception exception;
    private Map<String, String> errors;
}