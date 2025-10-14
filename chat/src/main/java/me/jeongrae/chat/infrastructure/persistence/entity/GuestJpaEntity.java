package me.jeongrae.chat.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jeongrae.chat.domain.authn.member.Guest;

@Entity
@Table(name = "guest")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuestJpaEntity {

    @Id
    @Column(name = "guest_id", length = 36)
    private String id;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    public static GuestJpaEntity fromDomain(Guest guest) {
        GuestJpaEntity entity = new GuestJpaEntity();
        entity.id = guest.id().value();
        entity.nickname = guest.nickname();
        return entity;
    }
}
