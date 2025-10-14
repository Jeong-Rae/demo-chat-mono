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

import java.lang.reflect.Field;
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
        try {
            MemberId memberId = MemberId.of(entity.getId());
            HashedPassword hashedPassword = HashedPassword.of(entity.getHashedPassword());

            // Using reflection to bypass the public factory method's logic
            Member member = (Member) Member.class.getDeclaredConstructors()[0].newInstance(
                    memberId,
                    entity.getUsername(),
                    entity.getNickname(),
                    hashedPassword
            );

            // Set the ID using reflection since the constructor in the Entity superclass handles it
            Field idField = Member.class.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, memberId);

            return member;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map MemberJpaEntity to Member domain object", e);
        }
    }
}
