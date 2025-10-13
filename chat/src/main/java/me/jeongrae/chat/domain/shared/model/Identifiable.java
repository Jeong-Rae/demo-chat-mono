package me.jeongrae.chat.domain.shared.model;

/**
 * 식별자를 노출하는 도메인 객체의 공통 인터페이스.
 */
public interface Identifiable<ID> {
    ID id();
}