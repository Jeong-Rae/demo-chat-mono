package me.jeongrae.chat.common.guard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Guard 일반 유틸리티 테스트")
class GuardTest {

    @DisplayName("notNull: null이 아닌 경우 값을 반환한다")
    @Test
    void notNull_shouldReturnValue_whenNotNull() {
        String value = "test";
        assertEquals(value, Guard.notNull(value));
        assertEquals(value, Guard.notNull(value, () -> "메시지"));
    }

    @DisplayName("notNull: null인 경우 NullPointerException을 발생시킨다")
    @Test
    void notNull_shouldThrowNullPointerException_whenNull() {
        assertThrows(NullPointerException.class, () -> Guard.notNull(null));
        assertThrows(NullPointerException.class, () -> Guard.notNull(null, () -> "널입니다."));
    }

    @DisplayName("isTrue: 조건이 참인 경우 예외를 발생시키지 않는다")
    @Test
    void isTrue_shouldNotThrowException_whenTrue() {
        assertDoesNotThrow(() -> Guard.isTrue(true));
        assertDoesNotThrow(() -> Guard.isTrue(true, () -> "거짓입니다."));
    }

    @DisplayName("isTrue: 조건이 거짓인 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void isTrue_shouldThrowIllegalArgumentException_whenFalse() {
        assertThrows(IllegalArgumentException.class, () -> Guard.isTrue(false));
        assertThrows(IllegalArgumentException.class, () -> Guard.isTrue(false, () -> "거짓입니다."));
    }

    @DisplayName("state: 상태가 유효한 경우 예외를 발생시키지 않는다")
    @Test
    void state_shouldNotThrowException_whenTrue() {
        assertDoesNotThrow(() -> Guard.state(true));
        assertDoesNotThrow(() -> Guard.state(true, () -> "유효하지 않은 상태"));
    }

    @DisplayName("state: 상태가 유효하지 않은 경우 IllegalStateException을 발생시킨다")
    @Test
    void state_shouldThrowIllegalStateException_whenFalse() {
        assertThrows(IllegalStateException.class, () -> Guard.state(false));
        assertThrows(IllegalStateException.class, () -> Guard.state(false, () -> "유효하지 않은 상태"));
    }

    @DisplayName("unsupported: 항상 UnsupportedOperationException을 발생시킨다")
    @Test
    void unsupported_shouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> Guard.unsupported());
        assertThrows(UnsupportedOperationException.class, () -> Guard.unsupported(() -> "지원하지 않는 작업"));
    }

    @DisplayName("fail: 항상 IllegalArgumentException을 발생시킨다")
    @Test
    void fail_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Guard.fail());
        assertThrows(IllegalArgumentException.class, () -> Guard.fail(() -> "실패"));
    }
}