package me.jeongrae.chat.domain.shared.error;

public enum ChatErrorCode implements DomainErrorCode {
    SENDER_NOT_PARTICIPANT("채팅방 참여자가 아닌 발신자입니다."),
    MESSAGE_ROOM_MISMATCH("메시지가 현재 채팅방에 속해있지 않습니다."),
    INVALID_PARTICIPANTS("채팅방 참여자는 서로 달라야 합니다."),

    // Credential Policy Errors
    USERNAME_CANNOT_BE_BLANK("사용자 이름은 비워둘 수 없습니다."),
    INVALID_USERNAME_FORMAT("사용자 이름은 4-20자의 영문 소문자, 숫자로만 구성되어야 합니다."),
    NICKNAME_CANNOT_BE_BLANK("닉네임은 비워둘 수 없습니다."),
    PASSWORD_CANNOT_BE_NULL("비밀번호는 null일 수 없습니다."),
    PASSWORD_TOO_WEAK("비밀번호가 너무 약합니다. 중간 이상의 강도가 필요합니다."),

    // Registration Errors
    USERNAME_OR_NICKNAME_ALREADY_EXISTS("이미 사용 중인 사용자 이름 또는 닉네임입니다.");

    private final String defaultMessage;

    ChatErrorCode(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String defaultMessage() {
        return defaultMessage;
    }

    public DomainException ex() {
        return DomainException.of(this);
    }

    public DomainException ex(String message) {
        return DomainException.of(this, message);
    }

    public DomainException ex(ErrorDetails details) {
        return DomainException.of(this, details);
    }

    public DomainException ex(String message, ErrorDetails details) {
        return DomainException.of(this, message, details);
    }

    public DomainException ex(Object... kvPairs) {
        return DomainException.of(this, ErrorDetails.of(kvPairs));
    }
}
