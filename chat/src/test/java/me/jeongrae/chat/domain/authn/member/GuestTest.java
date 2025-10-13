package me.jeongrae.chat.domain.authn.member;

import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.authn.policy.PasswordPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Guest 애그리게이트 테스트")
class GuestTest {

    @DisplayName("register: 정책에 부합하는 비밀번호로 Guest를 생성한다")
    @Test
    void register_shouldCreateGuest_whenPasswordSatisfiesPolicy() {
        // given
        GuestId guestId = GuestId.of("guest-1");
        String username = "guest-user";
        Password password = Password.of("SecurePass1!");
        RecordingPasswordPolicy passwordPolicy = new RecordingPasswordPolicy();

        // when
        Guest guest = Guest.register(guestId, username, password, passwordPolicy);

        // then
        assertAll(
            () -> assertEquals(guestId, guest.id()),
            () -> assertEquals(username, guest.username()),
            () -> assertEquals(passwordPolicy.hash(password), guest.hashedPassword())
        );
    }

    @DisplayName("authenticate: 저장된 해시와 일치하면 true를 반환한다")
    @Test
    void authenticate_shouldReturnTrue_whenPasswordMatches() {
        // given
        GuestId guestId = GuestId.of("guest-3");
        Password password = Password.of("SecurePass1!");
        RecordingPasswordPolicy passwordPolicy = new RecordingPasswordPolicy();
        Guest guest = Guest.register(guestId, "guest-user", password, passwordPolicy);

        // when
        boolean actual = guest.authenticate(password, passwordPolicy);

        // then
        assertTrue(actual);
    }

    @DisplayName("authenticate: 저장된 해시와 일치하지 않으면 false를 반환한다")
    @Test
    void authenticate_shouldReturnFalse_whenPasswordDoesNotMatch() {
        // given
        GuestId guestId = GuestId.of("guest-4");
        Password password = Password.of("SecurePass1!");
        RecordingPasswordPolicy passwordPolicy = new RecordingPasswordPolicy();
        Guest guest = Guest.register(guestId, "guest-user", password, passwordPolicy);
        Password wrongPassword = Password.of("WrongPass3#");

        // when
        boolean actual = guest.authenticate(wrongPassword, passwordPolicy);

        // then
        assertFalse(actual);
    }

    @DisplayName("register: 약함 기준만 충족해도 Guest를 생성한다")
    @Test
    void register_shouldCreateGuest_whenPasswordMeetsWeakCriteria() {
        // given
        GuestId guestId = GuestId.of("guest-5");
        String username = "weak-guest";
        Password weakPassword = Password.of("abcd");
        RecordingPasswordPolicy passwordPolicy = new RecordingPasswordPolicy();

        // when
        Guest guest = Guest.register(guestId, username, weakPassword, passwordPolicy);

        // then
        assertAll(
            () -> assertEquals(guestId, guest.id()),
            () -> assertEquals(username, guest.username()),
            () -> assertEquals(passwordPolicy.hash(weakPassword), guest.hashedPassword())
        );
    }

    @DisplayName("register: 약함 기준을 만족하지 못하면 예외가 발생한다")
    @Test
    void register_shouldThrow_whenPasswordDoesNotMeetWeakCriteria() {
        // given
        GuestId guestId = GuestId.of("guest-6");
        String username = "invalid-guest";
        Password invalidPassword = Password.of("abc");
        RecordingPasswordPolicy passwordPolicy = new RecordingPasswordPolicy();

        // when & then
        assertThrows(IllegalArgumentException.class,
            () -> Guest.register(guestId, username, invalidPassword, passwordPolicy));
    }

    private static final class RecordingPasswordPolicy implements PasswordPolicy {

        @Override
        public HashedPassword hash(Password password) {
            return HashedPassword.of("hashed:" + password.value());
        }

        @Override
        public boolean matches(Password password, HashedPassword hashedPassword) {
            return hash(password).equals(hashedPassword);
        }
    }
}
