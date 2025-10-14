package me.jeongrae.chat.infrastructure.persistence;

import me.jeongrae.chat.domain.authn.member.Guest;
import me.jeongrae.chat.domain.authn.member.GuestId;
import me.jeongrae.chat.domain.authn.repository.GuestRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryGuestRepository implements GuestRepository {

    private final Map<GuestId, Guest> store = new ConcurrentHashMap<>();

    @Override
    public Guest save(Guest guest) {
        store.put(guest.id(), guest);
        return guest;
    }
}
