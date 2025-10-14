package me.jeongrae.chat.infrastructure.generator;

import me.jeongrae.chat.domain.authn.member.GuestId;
import me.jeongrae.chat.domain.authn.port.GuestIdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDGuestIdGenerator implements GuestIdGenerator {

    @Override
    public GuestId generate() {
        return GuestId.of(UUID.randomUUID().toString());
    }
}
