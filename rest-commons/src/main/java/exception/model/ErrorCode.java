package exception.model;

public enum ErrorCode {
    AUTHENTICATION_ERROR,
    ACCESS_DENIED,
    BLOCKED_USER,
    USER_DELETED_OR_DOES_NOT_EXIST,
    INTERNAL_SERVER_ERROR,

    // jwt
    INVALID_TOKEN,
    EXPIRED_TOKEN
}