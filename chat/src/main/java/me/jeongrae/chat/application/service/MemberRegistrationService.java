package me.jeongrae.chat.application.service;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.application.RegisterMemberCommand;
import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.authn.member.Member;
import me.jeongrae.chat.domain.authn.member.MemberId;
import me.jeongrae.chat.domain.authn.policy.CredentialPolicy;
import me.jeongrae.chat.domain.authn.policy.PasswordHasher;
import me.jeongrae.chat.domain.authn.repository.MemberRepository;
import me.jeongrae.chat.domain.shared.error.ChatErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberRegistrationService {

    private final MemberRepository memberRepository;
    private final CredentialPolicy credentialPolicy;
    private final PasswordHasher passwordHasher;

    @Transactional
    public MemberId register(RegisterMemberCommand command) {
        if (memberRepository.existsByUsernameOrNickname(command.username(), command.nickname())) {
            throw ChatErrorCode.USERNAME_OR_NICKNAME_ALREADY_EXISTS.ex();
        }

        MemberId memberId = MemberId.of(UUID.randomUUID().toString());
        Password password = Password.of(command.password());

        Member newMember = Member.register(memberId, command.username(), command.nickname(),
                password, credentialPolicy, passwordHasher);

        memberRepository.save(newMember);

        return newMember.id();
    }
}
