package me.jeongrae.chat.application.error;

public class ApplicationException extends RuntimeException {
    private final ApplicationErrorCode code;

    private ApplicationException(ApplicationErrorCode code, String message) {
        super(message != null ? message : code.defaultMessage());
        this.code = code;
    }

    public static ApplicationException of(ApplicationErrorCode code) {
        return new ApplicationException(code, null);
    }

    public static ApplicationException of(ApplicationErrorCode code, String message) {
        return new ApplicationException(code, message);
    }

    public ApplicationErrorCode code() {
        return code;
    }
}
