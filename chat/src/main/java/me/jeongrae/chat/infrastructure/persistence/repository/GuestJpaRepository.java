package me.jeongrae.chat.infrastructure.persistence.repository;

import me.jeongrae.chat.infrastructure.persistence.entity.GuestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestJpaRepository extends JpaRepository<GuestJpaEntity, String> {
}
