package me.jeongrae.chat.domain.authn.repository;

import me.jeongrae.chat.domain.authn.member.Member;
import me.jeongrae.chat.domain.authn.member.MemberId;

import java.util.Optional;

/**
 * Member 애그리게이트 Repository.
 */
public interface MemberRepository {

    /**
     * Member를 저장.
     *
     * @param member 저장할 Member
     * @return 저장된 Member
     */
    Member save(Member member);

    /**
     * 사용자 이름으로 Member 조회.
     *
     * @param username 사용자 이름
     * @return Optional<Member>
     */
    Optional<Member> findByUsername(String username);

    /**
     * 사용자 이름 또는 닉네임 존재 여부 확인.
     *
     * @param username 사용자 이름
     * @param nickname 닉네임
     * @return 존재하면 true
     */
    boolean existsByUsernameOrNickname(String username, String nickname);

    /**
     * ID로 Member 조회.
     *
     * @param memberId Member ID
     * @return Optional<Member>
     */
    Optional<Member> findById(MemberId memberId);
}
