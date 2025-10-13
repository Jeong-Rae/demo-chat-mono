package me.jeongrae.chat.common.guard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GuardString 유틸리티 테스트")
class GuardStringTest {

    @DisplayName("notBlank: 문자열이 공백이 아닌 경우 값을 반환한다")
    @Test
    void notBlank_shouldReturnValue_whenNotBlank() {
        String value = "  hello  ";
        assertEquals(value, Guard.notBlank(value));
        assertEquals(value, Guard.notBlank(value, () -> "공백입니다."));
    }

    @DisplayName("notBlank: 문자열이 공백인 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void notBlank_shouldThrowIllegalArgumentException_whenBlank() {
        assertThrows(IllegalArgumentException.class, () -> Guard.notBlank(" "));
        assertThrows(IllegalArgumentException.class, () -> Guard.notBlank(" ", () -> "공백입니다."));
        assertThrows(IllegalArgumentException.class, () -> Guard.notBlank(null, () -> "널입니다."));
    }

    @DisplayName("hasText: 문자열이 텍스트를 포함하는 경우 값을 반환한다")
    @Test
    void hasText_shouldReturnValue_whenHasText() {
        String value = "  hello  ";
        assertEquals(value, Guard.hasText(value));
        assertEquals(value, Guard.hasText(value, () -> "공백입니다."));
    }

    @DisplayName("hasText: 문자열이 텍스트를 포함하지 않는 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void hasText_shouldThrowIllegalArgumentException_whenNoText() {
        assertThrows(IllegalArgumentException.class, () -> Guard.hasText(" "));
        assertThrows(IllegalArgumentException.class, () -> Guard.hasText(" ", () -> "공백입니다."));
        assertThrows(IllegalArgumentException.class, () -> Guard.hasText(null, () -> "널입니다."));
    }

    @DisplayName("lengthBetween: 문자열 길이가 범위 내에 있는 경우 값을 반환한다")
    @Test
    void lengthBetween_shouldReturnValue_whenLengthInRange() {
        String value = "hello";
        assertEquals(value, Guard.lengthBetween(value, 3, 7));
        assertEquals(value, Guard.lengthBetween(value, 3, 7, () -> "길이 오류"));
    }

    @DisplayName("lengthBetween: 문자열 길이가 범위를 벗어나는 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void lengthBetween_shouldThrowIllegalArgumentException_whenLengthOutOfRange() {
        String value = "hello";
        assertThrows(IllegalArgumentException.class, () -> Guard.lengthBetween(value, 1, 3));
        assertThrows(IllegalArgumentException.class, () -> Guard.lengthBetween(value, 1, 3, () -> "길이 오류"));
        assertThrows(IllegalArgumentException.class, () -> Guard.lengthBetween(value, 7, 10));
        assertThrows(IllegalArgumentException.class, () -> Guard.lengthBetween(value, 7, 10, () -> "길이 오류"));
    }

    @DisplayName("matches: 문자열이 정규식과 일치하는 경우 값을 반환한다")
    @Test
    void matches_shouldReturnValue_whenMatchesRegex() {
        String value = "hello";
        assertEquals(value, Guard.matches(value, "h.*o"));
        assertEquals(value, Guard.matches(value, "h.*o", () -> "정규식 불일치"));
    }

    @DisplayName("matches: 문자열이 정규식과 일치하지 않는 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void matches_shouldThrowIllegalArgumentException_whenNotMatchesRegex() {
        String value = "hello";
        assertThrows(IllegalArgumentException.class, () -> Guard.matches(value, "a.*b"));
        assertThrows(IllegalArgumentException.class, () -> Guard.matches(value, "a.*b", () -> "정규식 불일치"));
    }
}
