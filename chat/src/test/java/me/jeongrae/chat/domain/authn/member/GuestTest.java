package me.jeongrae.chat.domain.authn.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GuestTest {

    @Test
    @DisplayName("Guest 생성 성공")
    void of_Success() {
        // given
        GuestId guestId = GuestId.of("guest123");
        String nickname = "guestUser";

        // when
        Guest guest = Guest.of(guestId, nickname);

        // then
        assertThat(guest).isNotNull();
        assertThat(guest.id()).isEqualTo(guestId);
        assertThat(guest.nickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("Guest 생성 실패 - nickname 비어있음")
    void of_Failure_BlankNickname() {
        // given
        GuestId guestId = GuestId.of("guest123");
        String nickname = "";

        // when & then
        assertThatThrownBy(() -> Guest.of(guestId, nickname))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Guest 생성 실패 - guestId가 null")
    void of_Failure_NullGuestId() {
        // given
        String nickname = "guestUser";

        // when & then
        assertThatThrownBy(() -> Guest.of(null, nickname)).isInstanceOf(NullPointerException.class);
    }
}
