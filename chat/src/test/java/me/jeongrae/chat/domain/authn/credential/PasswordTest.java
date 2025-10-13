package me.jeongrae.chat.domain.authn.credential;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Password 값 객체 테스트")
class PasswordTest {

    @DisplayName("of: 값이 주어지면 Password를 생성한다")
    @Test
    void of_shouldCreatePassword_whenValueProvided() {
        // given
        String rawPassword = "SecurePass1!";

        // when
        Password password = Password.of(rawPassword);

        // then
        assertEquals(rawPassword, password.value());
    }

    @DisplayName("of: 값이 비어있으면 예외가 발생한다")
    @Test
    void of_shouldThrowException_whenValueBlank() {
        // given
        String blankPassword = " ";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> Password.of(blankPassword));
    }

    @DisplayName("strength: 강함 기준을 충족하면 STRONG을 반환한다")
    @Test
    void strength_shouldReturnStrong_whenStrongCriteriaSatisfied() {
        // given
        Password strongPassword = Password.of("Abcd1234!");

        // when
        PasswordStrength strength = strongPassword.strength();

        // then
        assertEquals(PasswordStrength.STRONG, strength);
    }

    @DisplayName("strength: 대문자를 제외한 8자리 조합이면 MEDIUM을 반환한다")
    @Test
    void strength_shouldReturnMedium_whenMediumCriteriaSatisfied() {
        // given
        Password mediumPassword = Password.of("abcd1234!");

        // when
        PasswordStrength strength = mediumPassword.strength();

        // then
        assertEquals(PasswordStrength.MEDIUM, strength);
    }

    @DisplayName("strength: 4자리 이상이면 WEAK을 반환한다")
    @Test
    void strength_shouldReturnWeak_whenWeakCriteriaSatisfied() {
        // given
        Password weakPassword = Password.of("abcd");

        // when
        PasswordStrength strength = weakPassword.strength();

        // then
        assertEquals(PasswordStrength.WEAK, strength);
    }

    @DisplayName("strength: 허용되지 않은 문자가 포함되면 예외가 발생한다")
    @Test
    void strength_shouldThrow_whenDisallowedCharactersIncluded() {
        // given
        Password invalidPassword = Password.of("abc d");

        // when & then
        assertThrows(IllegalArgumentException.class, invalidPassword::strength);
    }
}
