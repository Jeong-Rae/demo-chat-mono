package me.jeongrae.chat.domain.authn.repository;

import me.jeongrae.chat.domain.authn.member.Guest;

/**
 * Guest 애그리게이트에 대한 Repository 인터페이스입니다.
 */
public interface GuestRepository {

    /**
     * Guest를 저장합니다.
     *
     * @param guest 저장할 Guest
     * @return 저장된 Guest
     */
    Guest save(Guest guest);
}
