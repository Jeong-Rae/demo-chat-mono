package me.jeongrae.chat.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.member.Member;
import me.jeongrae.chat.domain.authn.member.MemberId;
import me.jeongrae.chat.domain.authn.repository.MemberRepository;
import me.jeongrae.chat.infrastructure.persistence.entity.MemberJpaEntity;
import me.jeongrae.chat.infrastructure.persistence.repository.MemberJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {

    private final MemberJpaRepository jpaRepository;

    @Override
    public Member save(Member member) {
        MemberJpaEntity entity = MemberJpaEntity.fromDomain(member);
        jpaRepository.save(entity);
        return member;
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(this::toDomain);
    }

    @Override
    public boolean existsByUsernameOrNickname(String username, String nickname) {
        return jpaRepository.existsByUsernameOrNickname(username, nickname);
    }

    @Override
    public Optional<Member> findById(MemberId memberId) {
        return jpaRepository.findById(memberId.value()).map(this::toDomain);
    }

    private Member toDomain(MemberJpaEntity entity) {
        return Member.of(MemberId.of(entity.getId()), entity.getUsername(), entity.getNickname(),
                HashedPassword.of(entity.getHashedPassword()));
    }
}
