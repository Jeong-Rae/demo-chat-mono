package me.jeongrae.chat.application.service;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.application.error.ApplicationErrorCode;
import me.jeongrae.chat.domain.authn.member.Member;
import me.jeongrae.chat.domain.authn.member.MemberId;
import me.jeongrae.chat.domain.authn.repository.MemberRepository;
import me.jeongrae.chat.infrastructure.persistence.entity.RefreshToken;
import me.jeongrae.chat.infrastructure.persistence.repository.RefreshTokenRepository;
import me.jeongrae.chat.infrastructure.security.JwtProvider;
import me.jeongrae.chat.interfaces.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenRefreshService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Transactional
    public TokenResponse refreshTokens(String refreshTokenValue) {

        RefreshToken refreshToken =
                refreshTokenRepository.findByToken(refreshTokenValue).orElseThrow(
                        () -> ApplicationErrorCode.INVALID_REFRESH_TOKEN.ex("올바르지 않은 리프레시 토큰입니다."));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw ApplicationErrorCode.INVALID_REFRESH_TOKEN.ex("만료된 리프레시 토큰입니다.");
        }

        String memberId = refreshToken.getMemberId();
        Member member = memberRepository.findById(MemberId.of(memberId)).orElseThrow(
                () -> ApplicationErrorCode.INVALID_REFRESH_TOKEN.ex("올바르지 않은 리프레시 토큰입니다."));

        String newAccessToken = jwtProvider.generateAccessToken(member);
        String newRefreshTokenValue = UUID.randomUUID().toString();

        refreshToken.updateToken(newRefreshTokenValue,
                Instant.now().plusMillis(refreshTokenExpirationMs));
        refreshTokenRepository.save(refreshToken);

        return TokenResponse.of(newAccessToken, newRefreshTokenValue);
    }
}
