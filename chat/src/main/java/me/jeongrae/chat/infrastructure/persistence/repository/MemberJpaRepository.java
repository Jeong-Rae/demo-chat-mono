package me.jeongrae.chat.infrastructure.persistence.repository;

import me.jeongrae.chat.infrastructure.persistence.entity.MemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, String> {

    Optional<MemberJpaEntity> findByUsername(String username);

    boolean existsByUsernameOrNickname(String username, String nickname);
}
