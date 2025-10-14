package me.jeongrae.chat.application.service;

import me.jeongrae.chat.application.RegisterMemberCommand;
import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.authn.member.Member;
import me.jeongrae.chat.domain.authn.policy.CredentialPolicy;
import me.jeongrae.chat.domain.authn.policy.PasswordHasher;
import me.jeongrae.chat.domain.authn.repository.MemberRepository;
import me.jeongrae.chat.domain.shared.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberRegistrationServiceTest {

    @InjectMocks
    private MemberRegistrationService memberRegistrationService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CredentialPolicy credentialPolicy;

    @Mock
    private PasswordHasher passwordHasher;

    @Test
    @DisplayName("회원 가입 성공")
    void register_Success() {
        // given
        RegisterMemberCommand command =
                new RegisterMemberCommand("testuser", "tester", "password123!");
        when(memberRepository.existsByUsernameOrNickname(anyString(), anyString()))
                .thenReturn(false);
        when(passwordHasher.hash(any(Password.class)))
                .thenReturn(HashedPassword.of("hashed-password"));

        // when
        memberRegistrationService.register(command);

        // then
        verify(credentialPolicy, times(1)).check(eq("testuser"), eq("tester"), any(Password.class));
        verify(passwordHasher, times(1)).hash(any(Password.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 가입 실패 - 사용자 이름 또는 닉네임 중복")
    void register_Failure_DuplicateUsernameOrNickname() {
        // given
        RegisterMemberCommand command =
                new RegisterMemberCommand("testuser", "tester", "password123!");
        when(memberRepository.existsByUsernameOrNickname("testuser", "tester")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberRegistrationService.register(command))
                .isInstanceOf(DomainException.class);

        verify(memberRepository, never()).save(any(Member.class));
    }
}
