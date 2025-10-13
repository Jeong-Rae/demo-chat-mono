package me.jeongrae.chat.domain.authn.credential;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("HashedPassword 값 객체 테스트")
class HashedPasswordTest {

    @DisplayName("of: 값이 주어지면 HashedPassword를 생성한다")
    @Test
    void of_shouldCreateHashedPassword_whenValueProvided() {
        // given
        String hashedValue = "hashed-value";

        // when
        HashedPassword hashedPassword = HashedPassword.of(hashedValue);

        // then
        assertEquals(hashedValue, hashedPassword.value());
    }

    @DisplayName("of: 값이 비어있으면 예외가 발생한다")
    @Test
    void of_shouldThrowException_whenValueBlank() {
        // given
        String blankValue = "";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> HashedPassword.of(blankValue));
    }
}
