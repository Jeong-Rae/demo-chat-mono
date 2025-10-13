package me.jeongrae.chat.domain.shared.error;

public interface DomainErrorCode {
    /**
     * 기본 메시지
     */
    String defaultMessage();

    /**
     * 직렬화 및 로깅에 사용될 식별자
     */
    String name();
}
