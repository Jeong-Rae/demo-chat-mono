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
        String username = "guestUser";

        // when
        Guest guest = Guest.of(guestId, username);

        // then
        assertThat(guest).isNotNull();
        assertThat(guest.id()).isEqualTo(guestId);
        assertThat(guest.username()).isEqualTo(username);
    }

    @Test
    @DisplayName("Guest 생성 실패 - username이 비어있음")
    void of_Failure_BlankUsername() {
        // given
        GuestId guestId = GuestId.of("guest123");
        String username = "";

        // when & then
        assertThatThrownBy(() -> Guest.of(guestId, username))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Guest 생성 실패 - guestId가 null")
    void of_Failure_NullGuestId() {
        // given
        String username = "guestUser";

        // when & then
        assertThatThrownBy(() -> Guest.of(null, username))
            .isInstanceOf(NullPointerException.class);
    }
}
