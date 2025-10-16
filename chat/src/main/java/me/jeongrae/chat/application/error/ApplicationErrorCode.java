package me.jeongrae.chat.application.error;

public enum ApplicationErrorCode {
    INVALID_REFRESH_TOKEN("유효하지 않은 리프레시 토큰입니다.");

    private final String defaultMessage;

    ApplicationErrorCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String defaultMessage() {
        return defaultMessage;
    }

    public ApplicationException ex() {
        return ApplicationException.of(this);
    }

    public ApplicationException ex(String message) {
        return ApplicationException.of(this, message);
    }
}
