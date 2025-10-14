package me.jeongrae.chat.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.domain.authn.member.Guest;
import me.jeongrae.chat.domain.authn.repository.GuestRepository;
import me.jeongrae.chat.infrastructure.persistence.entity.GuestJpaEntity;
import me.jeongrae.chat.infrastructure.persistence.repository.GuestJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class GuestRepositoryAdapter implements GuestRepository {

    private final GuestJpaRepository jpaRepository;

    @Override
    public Guest save(Guest guest) {
        GuestJpaEntity entity = GuestJpaEntity.fromDomain(guest);
        jpaRepository.save(entity);
        return guest;
    }
}
