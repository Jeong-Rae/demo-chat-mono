package me.jeongrae.chat.application.service;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.application.GuestLoginCommand;
import me.jeongrae.chat.application.MemberLoginCommand;
import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.authn.member.Guest;
import me.jeongrae.chat.domain.authn.member.GuestId;
import me.jeongrae.chat.domain.authn.member.Member;
import me.jeongrae.chat.domain.authn.policy.PasswordHasher;
import me.jeongrae.chat.domain.authn.port.GuestIdGenerator;
import me.jeongrae.chat.domain.authn.repository.GuestRepository;
import me.jeongrae.chat.domain.authn.repository.MemberRepository;
import me.jeongrae.chat.domain.shared.error.ChatErrorCode;
import me.jeongrae.chat.infrastructure.persistence.entity.RefreshToken;
import me.jeongrae.chat.infrastructure.persistence.repository.RefreshTokenRepository;
import me.jeongrae.chat.infrastructure.security.JwtProvider;
import me.jeongrae.chat.interfaces.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final MemberRepository memberRepository;
    private final GuestRepository guestRepository;
    private final PasswordHasher passwordHasher;
    private final JwtProvider jwtProvider;
    private final GuestIdGenerator guestIdGenerator;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Transactional
    public TokenResponse loginMember(MemberLoginCommand command) {
        Member member = memberRepository.findByUsername(command.username())
                .orElseThrow(() -> ChatErrorCode.INVALID_CREDENTIALS.ex());

        Password password = Password.of(command.password());
        if (!member.authenticate(password, passwordHasher)) {
            throw ChatErrorCode.INVALID_CREDENTIALS.ex();
        }

        String accessToken = jwtProvider.generateAccessToken(member);
        String refreshTokenValue = jwtProvider.generateRefreshToken(member);

        refreshTokenRepository.deleteByMemberId(member.id().value());
        RefreshToken refreshToken = RefreshToken.of(member.id().value(), refreshTokenValue,
                Instant.now().plusMillis(refreshTokenExpirationMs));
        refreshTokenRepository.save(refreshToken);

        return TokenResponse.of(accessToken, refreshTokenValue);
    }

    @Transactional
    public TokenResponse loginGuest(GuestLoginCommand command) {
        GuestId guestId = guestIdGenerator.generate();
        Guest guest = Guest.of(guestId, command.nickname());
        guestRepository.save(guest);

        String accessToken = jwtProvider.generateAccessToken(guest);

        return TokenResponse.accessTokenOnly(accessToken);
    }
}
