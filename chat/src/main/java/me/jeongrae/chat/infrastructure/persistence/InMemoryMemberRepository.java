package me.jeongrae.chat.infrastructure.persistence;

import me.jeongrae.chat.domain.authn.member.Member;
import me.jeongrae.chat.domain.authn.member.MemberId;
import me.jeongrae.chat.domain.authn.repository.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryMemberRepository implements MemberRepository {

    private final Map<MemberId, Member> store = new ConcurrentHashMap<>();
    private final Map<String, MemberId> usernameIndex = new ConcurrentHashMap<>();
    private final Map<String, MemberId> nicknameIndex = new ConcurrentHashMap<>();

    @Override
    public Member save(Member member) {
        store.put(member.id(), member);
        usernameIndex.put(member.username(), member.id());
        nicknameIndex.put(member.nickname(), member.id());
        return member;
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return Optional.ofNullable(usernameIndex.get(username)).map(store::get);
    }

    @Override
    public boolean existsByUsernameOrNickname(String username, String nickname) {
        return usernameIndex.containsKey(username) || nicknameIndex.containsKey(nickname);
    }

    @Override
    public Optional<Member> findById(MemberId memberId) {
        return Optional.ofNullable(store.get(memberId));
    }
}
