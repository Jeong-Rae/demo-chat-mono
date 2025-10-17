package me.jeongrae.chat.infrastructure.security;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.domain.authn.member.Member;
import me.jeongrae.chat.domain.authn.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username).map(this::toUserDetails).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username));
    }

    private UserDetails toUserDetails(Member member) {
        return User.builder().username(member.username()).password(member.hashedPassword().value())
                .authorities(Collections.emptyList()) // TODO: Role이나 Auth 추가해야함
                .build();
    }
}
