package me.jeongrae.chat.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jeongrae.chat.domain.authn.member.Member;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberJpaEntity {

    @Id
    @Column(name = "member_id", length = 36)
    private String id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "nickname", unique = true, nullable = false, length = 50)
    private String nickname;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    public static MemberJpaEntity fromDomain(Member member) {
        MemberJpaEntity entity = new MemberJpaEntity();
        entity.id = member.id().value();
        entity.username = member.username();
        entity.nickname = member.nickname();
        entity.hashedPassword = member.hashedPassword().value();
        return entity;
    }
}
