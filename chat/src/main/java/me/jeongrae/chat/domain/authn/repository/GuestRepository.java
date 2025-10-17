package me.jeongrae.chat.domain.authn.repository;

import me.jeongrae.chat.domain.authn.member.Guest;

/**
 * Guest 애그리게이트 Repository.
 */
public interface GuestRepository {

    /**
     * Guest를 저장.
     *
     * @param guest 저장할 Guest
     * @return 저장된 Guest
     */
    Guest save(Guest guest);
}
