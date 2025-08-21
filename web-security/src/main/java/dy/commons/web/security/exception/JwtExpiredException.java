package dy.commons.web.security.exception;

public class JwtExpiredException extends JwtValidationException {
    public JwtExpiredException(String message) {
        super(message);
    }
}