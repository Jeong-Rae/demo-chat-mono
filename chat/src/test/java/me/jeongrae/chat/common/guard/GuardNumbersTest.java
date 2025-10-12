import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import me.jeongrae.chat.common.guard.Guard;
import static me.jeongrae.chat.common.guard.Guard.*;

@DisplayName("GuardNumbers 유틸리티 테스트")
class GuardNumbersTest {

    @DisplayName("inRange: 숫자가 범위 내에 있는 경우 값을 반환한다")
    @Test
    void inRange_shouldReturnValue_whenInRange() {
        Integer value = 5;
        assertEquals(value, Guard.inRange(value, 1, 10));
        assertEquals(value, Guard.inRange(value, 1, 10, () -> "범위 오류"));
    }

    @DisplayName("inRange: 숫자가 범위를 벗어나는 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void inRange_shouldThrowIllegalArgumentException_whenOutOfRange() {
        Integer value = 15;
        assertThrows(IllegalArgumentException.class, () -> Guard.inRange(value, 1, 10));
        assertThrows(IllegalArgumentException.class, () -> Guard.inRange(value, 1, 10, () -> "범위 오류"));
    }

    @DisplayName("positive: 숫자가 양수인 경우 값을 반환한다")
    @Test
    void positive_shouldReturnValue_whenPositive() {
        Integer value = 10;
        assertEquals(value, Guard.positive(value));
        assertEquals(value, Guard.positive(value, () -> "양수 아님"));
    }

    @DisplayName("positive: 숫자가 양수가 아닌 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void positive_shouldThrowIllegalArgumentException_whenNotPositive() {
        Integer value = -5;
        assertThrows(IllegalArgumentException.class, () -> Guard.positive(value));
        assertThrows(IllegalArgumentException.class, () -> Guard.positive(value, () -> "양수 아님"));
        assertThrows(IllegalArgumentException.class, () -> Guard.positive(0));
    }

    @DisplayName("equals: 두 숫자가 같은 경우 값을 반환한다")
    @Test
    void equals_shouldReturnValue_whenEqual() {
        Integer value = 10;
        assertEquals(value, Guard.equals(value, 10));
        assertEquals(value, Guard.equals(value, 10, () -> "같지 않음"));
    }

    @DisplayName("equals: 두 숫자가 같지 않은 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void equals_shouldThrowIllegalArgumentException_whenNotEqual() {
        Integer value = 10;
        assertThrows(IllegalArgumentException.class, () -> Guard.equals(value, 5));
        assertThrows(IllegalArgumentException.class, () -> Guard.equals(value, 5, () -> "같지 않음"));
    }

    @DisplayName("notEquals: 두 숫자가 다른 경우 값을 반환한다")
    @Test
    void notEquals_shouldReturnValue_whenNotEqual() {
        Integer value = 10;
        assertEquals(value, Guard.notEquals(value, 5));
        assertEquals(value, Guard.notEquals(value, 5, () -> "같음"));
    }

    @DisplayName("notEquals: 두 숫자가 같은 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void notEquals_shouldThrowIllegalArgumentException_whenEqual() {
        Integer value = 10;
        assertThrows(IllegalArgumentException.class, () -> Guard.notEquals(value, 10));
        assertThrows(IllegalArgumentException.class, () -> Guard.notEquals(value, 10, () -> "같음"));
    }
}
