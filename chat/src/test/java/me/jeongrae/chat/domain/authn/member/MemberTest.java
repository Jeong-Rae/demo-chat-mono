package me.jeongrae.chat.domain.authn.member;

import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.authn.policy.CredentialPolicy;
import me.jeongrae.chat.domain.authn.policy.PasswordHasher;
import me.jeongrae.chat.domain.shared.error.ChatErrorCode;
import me.jeongrae.chat.domain.shared.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Member 애그리게이트 테스트")
class MemberTest {

    @Mock
    private CredentialPolicy credentialPolicy;

    @Mock
    private PasswordHasher passwordHasher;

    @Test
    @DisplayName("register: 정책을 통과하면 Member를 생성한다")
    void register_shouldCreateMember_whenPolicyPasses() {
        // given
        MemberId memberId = MemberId.of("member123");
        String username = "testuser";
        String nickname = "tester";
        Password password = Password.of("ValidPassword123!");
        HashedPassword hashedPassword = HashedPassword.of("hashed-password");

        doNothing().when(credentialPolicy).check(username, nickname, password);
        when(passwordHasher.hash(password)).thenReturn(hashedPassword);

        // when
        Member member = Member.register(memberId, username, nickname, password, credentialPolicy, passwordHasher);

        // then
        assertThat(member).isNotNull();
        assertThat(member.id()).isEqualTo(memberId);
        assertThat(member.username()).isEqualTo(username);
        assertThat(member.nickname()).isEqualTo(nickname);
        assertThat(member.hashedPassword()).isEqualTo(hashedPassword);

        verify(credentialPolicy).check(username, nickname, password);
        verify(passwordHasher).hash(password);
    }

    @Test
    @DisplayName("register: 자격 증명 정책 검사에 실패하면 예외가 발생한다")
    void register_shouldThrowException_whenCredentialPolicyFails() {
        // given
        MemberId memberId = MemberId.of("member123");
        String username = "invalid-user";
        String nickname = "tester";
        Password password = Password.of("weak");

        doThrow(ChatErrorCode.PASSWORD_TOO_WEAK.ex()).when(credentialPolicy).check(username, nickname, password);

        // when & then
        assertThatThrownBy(() -> Member.register(memberId, username, nickname, password, credentialPolicy, passwordHasher))
                .isInstanceOf(DomainException.class);

        verify(passwordHasher, never()).hash(any());
    }

    @Test
    @DisplayName("authenticate: 비밀번호가 일치하면 true를 반환한다")
    void authenticate_shouldReturnTrue_whenPasswordMatches() {
        // given
        MemberId memberId = MemberId.of("member123");
        String username = "testuser";
        String nickname = "tester";
        Password password = Password.of("ValidPassword123!");
        HashedPassword hashedPassword = HashedPassword.of("hashed-password");

        // Member를 생성하기 위해 register 과정을 모킹
        doNothing().when(credentialPolicy).check(username, nickname, password);
        when(passwordHasher.hash(password)).thenReturn(hashedPassword);
        Member member = Member.register(memberId, username, nickname, password, credentialPolicy, passwordHasher);

        // authenticate 호출에 대한 모킹 설정
        when(passwordHasher.matches(password, hashedPassword)).thenReturn(true);

        // when
        boolean isAuthenticated = member.authenticate(password, passwordHasher);

        // then
        assertThat(isAuthenticated).isTrue();
    }

    @Test
    @DisplayName("authenticate: 비밀번호가 일치하지 않으면 false를 반환한다")
    void authenticate_shouldReturnFalse_whenPasswordDoesNotMatch() {
        // given
        MemberId memberId = MemberId.of("member123");
        String username = "testuser";
        String nickname = "tester";
        Password password = Password.of("ValidPassword123!");
        Password wrongPassword = Password.of("WrongPassword");
        HashedPassword hashedPassword = HashedPassword.of("hashed-password");

        // Member를 생성하기 위해 register 과정을 모킹
        doNothing().when(credentialPolicy).check(username, nickname, password);
        when(passwordHasher.hash(password)).thenReturn(hashedPassword);
        Member member = Member.register(memberId, username, nickname, password, credentialPolicy, passwordHasher);

        // authenticate 호출에 대한 모킹 설정
        when(passwordHasher.matches(wrongPassword, hashedPassword)).thenReturn(false);

        // when
        boolean isAuthenticated = member.authenticate(wrongPassword, passwordHasher);

        // then
        assertThat(isAuthenticated).isFalse();
    }
}
