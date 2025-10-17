package me.jeongrae.chat.application;

import me.jeongrae.chat.common.guard.Guard;

/**
 * 게스트 로그인을 위한 커맨드 객체.
 *
 * @param nickname 닉네임
 */
public record GuestLoginCommand(String nickname) {
    public GuestLoginCommand {
        Guard.notBlank(nickname, "Nickname cannot be blank");
    }
}
