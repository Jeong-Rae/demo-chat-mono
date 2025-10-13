package me.jeongrae.chat.domain.shared.error;

public enum ChatErrorCode implements DomainErrorCode {
    SENDER_NOT_PARTICIPANT("채팅방 참여자가 아닌 발신자입니다."), MESSAGE_ROOM_MISMATCH(
            "메시지가 현재 채팅방에 속해있지 않습니다."), INVALID_PARTICIPANTS("채팅방 참여자는 서로 달라야 합니다.");

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
