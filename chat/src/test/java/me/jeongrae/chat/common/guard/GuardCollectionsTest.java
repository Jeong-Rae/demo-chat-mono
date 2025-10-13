package me.jeongrae.chat.common.guard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GuardCollections 유틸리티 테스트")
class GuardCollectionsTest {

    @DisplayName("notEmpty: 컬렉션이 비어있지 않은 경우 값을 반환한다")
    @Test
    void notEmpty_shouldReturnValue_whenNotEmpty() {
        List<String> list = Arrays.asList("a", "b");
        assertEquals(list, Guard.notEmpty(list));
        assertEquals(list, Guard.notEmpty(list, () -> "비어있음"));
    }

    @DisplayName("notEmpty: 컬렉션이 비어있는 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void notEmpty_shouldThrowIllegalArgumentException_whenEmpty() {
        List<String> list = Collections.emptyList();
        assertThrows(IllegalArgumentException.class, () -> Guard.notEmpty(list));
        assertThrows(IllegalArgumentException.class, () -> Guard.notEmpty(list, () -> "비어있음"));
    }

    @DisplayName("noNullElements: 컬렉션에 null 요소가 없는 경우 값을 반환한다")
    @Test
    void noNullElements_shouldReturnValue_whenNoNulls() {
        List<String> list = Arrays.asList("a", "b");
        assertEquals(list, Guard.noNullElements(list));
        assertEquals(list, Guard.noNullElements(list, () -> "null 요소 포함"));
    }

    @DisplayName("noNullElements: 컬렉션에 null 요소가 있는 경우 IllegalArgumentException을 발생시킨다")
    @Test
    void noNullElements_shouldThrowIllegalArgumentException_whenContainsNull() {
        List<String> list = Arrays.asList("a", null, "b");
        assertThrows(IllegalArgumentException.class, () -> Guard.noNullElements(list));
        assertThrows(IllegalArgumentException.class, () -> Guard.noNullElements(list, () -> "null 요소 포함"));
    }
}
