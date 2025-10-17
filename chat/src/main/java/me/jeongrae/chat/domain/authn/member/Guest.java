package me.jeongrae.chat.domain.authn.member;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;
import me.jeongrae.chat.domain.shared.model.Entity;

/**
 * 인증 컨텍스트의 Guest 애그리게이트 루트.
 */
@Getter
@Accessors(fluent = true)
public class Guest extends Entity<GuestId> {

    private final String nickname;

    private Guest(GuestId guestId, String nickname) {
        super(guestId);
        this.nickname =
                Guard.notBlank(nickname, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("nickname"));
    }

    public static Guest of(GuestId guestId, String nickname) {
        return new Guest(guestId, nickname);
    }
}
