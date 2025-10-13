package me.jeongrae.chat.domain.shared.error;

public class DomainException extends RuntimeException {
    private final DomainErrorCode code;
    private final ErrorDetails details;

    private DomainException(DomainErrorCode code, String message, ErrorDetails details) {
        super(message != null ? message : code.defaultMessage());
        this.code = code;
        this.details = details == null ? ErrorDetails.empty() : details;
    }

    public static DomainException of(DomainErrorCode code) {
        return new DomainException(code, null, ErrorDetails.empty());
    }

    public static DomainException of(DomainErrorCode code, String message) {
        return new DomainException(code, message, ErrorDetails.empty());
    }

    public static DomainException of(DomainErrorCode code, ErrorDetails details) {
        return new DomainException(code, null, details);
    }

    public static DomainException of(DomainErrorCode code, String message, ErrorDetails details) {
        return new DomainException(code, message, details);
    }

    public DomainErrorCode code() {
        return code;
    }

    public ErrorDetails details() {
        return details;
    }
}
