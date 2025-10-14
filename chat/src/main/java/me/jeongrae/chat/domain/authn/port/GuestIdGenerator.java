package me.jeongrae.chat.domain.authn.port;

import me.jeongrae.chat.domain.authn.member.GuestId;

/**
 * 고유한 Guest ID를 생성
 */
public interface GuestIdGenerator {

    /**
     * 새로운 GuestId를 생성
     * 
     * @return 고유한 GuestId
     */
    GuestId generate();
}
