package me.jeongrae.chat.domain.model;

/**
 * 객체에 능력·특성(capability)을 인터페이스 조합으로 부여한다.
 * @param <I>
 */
public interface Identifiable<I> {
    /**
     * 식별자를 반환한다.
     * @return
     */
    I id();
}
