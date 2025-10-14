package me.jeongrae.chat.domain.authn.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.authn.policy.PasswordPolicy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    private PasswordPolicy mockPasswordPolicy;

    @BeforeEach
    void setUp() {
        mockPasswordPolicy = new PasswordPolicy() {
            @Override
            public HashedPassword hash(Password password) {
                return HashedPassword.of("hashed-" + password.value());
            }

            @Override
            public boolean matches(Password password, HashedPassword hashedPassword) {
                return hashedPassword.value().equals("hashed-" + password.value());
            }
        };
    }

    @Test
    @DisplayName("Member 등록 성공")
    void register_Success() {
        // given
        MemberId memberId = MemberId.of("member123");
        String username = "testuser";
        String nickname = "tester";
        Password password = Password.of("ValidPassword123!"); // Medium strength

        // when
        Member member = Member.register(memberId, username, nickname, password, mockPasswordPolicy);

        // then
        assertThat(member).isNotNull();
        assertThat(member.id()).isEqualTo(memberId);
        assertThat(member.username()).isEqualTo(username);
        assertThat(member.nickname()).isEqualTo(nickname);
        assertThat(member.hashedPassword()).isNotNull();
    }

    @Test
    @DisplayName("Member 등록 실패 - 비밀번호 강도 부족")
    void register_Failure_WeakPassword() {
        // given
        MemberId memberId = MemberId.of("member123");
        String username = "testuser";
        String nickname = "tester";
        Password password = Password.of("weak"); // Weak strength

        // when & then
        assertThatThrownBy(() -> Member.register(memberId, username, nickname, password, mockPasswordPolicy))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Member 인증 성공")
    void authenticate_Success() {
        // given
        MemberId memberId = MemberId.of("member123");
        String username = "testuser";
        String nickname = "tester";
        Password password = Password.of("ValidPassword123!");
        Member member = Member.register(memberId, username, nickname, password, mockPasswordPolicy);

        // when
        boolean isAuthenticated = member.authenticate(password, mockPasswordPolicy);

        // then
        assertThat(isAuthenticated).isTrue();
    }

    @Test
    @DisplayName("Member 인증 실패 - 잘못된 비밀번호")
    void authenticate_Failure_WrongPassword() {
        // given
        MemberId memberId = MemberId.of("member123");
        String username = "testuser";
        String nickname = "tester";
        Password correctPassword = Password.of("ValidPassword123!");
        Password wrongPassword = Password.of("WrongPassword123!");
        Member member = Member.register(memberId, username, nickname, correctPassword, mockPasswordPolicy);

        // when
        boolean isAuthenticated = member.authenticate(wrongPassword, mockPasswordPolicy);

        // then
        assertThat(isAuthenticated).isFalse();
    }
    
    @Test
    @DisplayName("Member 생성 실패 - username이 비어있음")
    void register_Failure_BlankUsername() {
        // given
        MemberId memberId = MemberId.of("member123");
        String username = "";
        String nickname = "tester";
        Password password = Password.of("ValidPassword123!");

        // when & then
        assertThatThrownBy(() -> Member.register(memberId, username, nickname, password, mockPasswordPolicy))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Member 생성 실패 - nickname이 비어있음")
    void register_Failure_BlankNickname() {
        // given
        MemberId memberId = MemberId.of("member123");
        String username = "testuser";
        String nickname = "";
        Password password = Password.of("ValidPassword123!");

        // when & then
        assertThatThrownBy(() -> Member.register(memberId, username, nickname, password, mockPasswordPolicy))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
