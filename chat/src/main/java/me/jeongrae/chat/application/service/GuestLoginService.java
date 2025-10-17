package me.jeongrae.chat.application.service;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.application.GuestLoginCommand;
import me.jeongrae.chat.domain.authn.member.Guest;
import me.jeongrae.chat.domain.authn.member.GuestId;
import me.jeongrae.chat.domain.authn.port.GuestIdGenerator;
import me.jeongrae.chat.domain.authn.repository.GuestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GuestLoginService {

    private final GuestRepository guestRepository;
    private final GuestIdGenerator guestIdGenerator;

    @Transactional
    public Guest login(GuestLoginCommand command) {
        GuestId guestId = guestIdGenerator.generate();
        Guest guest = Guest.of(guestId, command.nickname());

        return guestRepository.save(guest);
    }
}
