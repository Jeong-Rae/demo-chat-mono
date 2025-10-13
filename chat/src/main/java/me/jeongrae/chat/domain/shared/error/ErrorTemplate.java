package me.jeongrae.chat.domain.shared.error;

public enum ErrorTemplate {
    VALUE_CANNOT_BE_EMPTY("%s 값은 비어있을 수 없습니다."),
    VALUE_CANNOT_BE_NULL("%s 값은 null일 수 없습니다."),
    PASSWORD_STRENGTH_REQUIREMENT("%s 비밀번호는 강도 요건을 만족해야 합니다.");

    private final String pattern;

    ErrorTemplate(String pattern) {
        this.pattern = pattern;
    }

    public String format(Object... args) {
        return String.format(pattern, args);
    }
}
