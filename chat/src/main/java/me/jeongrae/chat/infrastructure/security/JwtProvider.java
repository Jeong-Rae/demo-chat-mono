package me.jeongrae.chat.infrastructure.security;

import me.jeongrae.chat.domain.authn.member.Guest;
import me.jeongrae.chat.domain.authn.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;


@Component
public class JwtProvider {

    private final SecretKey key;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_TYPE_KEY = "type";
    private static final String USER_TYPE_MEMBER = "MEMBER";
    private static final String USER_TYPE_GUEST = "GUEST";

    public JwtProvider(@Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration-ms}") long accessTokenExpirationMs,
            @Value("${jwt.refresh-token-expiration-ms}") long refreshTokenExpirationMs) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(Member member) {
        return generateToken(member.id().value(), USER_TYPE_MEMBER, accessTokenExpirationMs);
    }

    public String generateRefreshToken(Member member) {
        return generateToken(member.id().value(), USER_TYPE_MEMBER, refreshTokenExpirationMs);
    }

    public String generateAccessToken(Guest guest) {
        return generateToken(guest.id().value(), USER_TYPE_GUEST, accessTokenExpirationMs);
    }

    private String generateToken(String subject, String userType, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder().subject(subject).claim(USER_TYPE_KEY, userType)
                .claim(AUTHORITIES_KEY, "").issuedAt(now).expiration(expiryDate).signWith(key)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private static final String INVALID_JWT_TOKEN_MESSAGE = "잘못된 JWT 토큰입니다.";
    private static final String EXPIRED_JWT_TOKEN_MESSAGE = "만료된 JWT 토큰입니다.";
    private static final String UNSUPPORTED_JWT_TOKEN_MESSAGE = "지원되지 않는 JWT 토큰입니다.";
    private static final String EMPTY_JWT_CLAIMS_MESSAGE = "JWT 클레임 문자열이 비어있습니다.";

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new InvalidJwtAuthenticationException(INVALID_JWT_TOKEN_MESSAGE, e);
        } catch (ExpiredJwtException e) {
            throw new InvalidJwtAuthenticationException(EXPIRED_JWT_TOKEN_MESSAGE, e);
        } catch (UnsupportedJwtException e) {
            throw new InvalidJwtAuthenticationException(UNSUPPORTED_JWT_TOKEN_MESSAGE, e);
        } catch (IllegalArgumentException e) {
            throw new InvalidJwtAuthenticationException(EMPTY_JWT_CLAIMS_MESSAGE, e);
        }
    }
}
