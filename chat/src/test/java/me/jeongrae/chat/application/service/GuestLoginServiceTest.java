package me.jeongrae.chat.application.service;

import me.jeongrae.chat.application.GuestLoginCommand;
import me.jeongrae.chat.domain.authn.member.Guest;
import me.jeongrae.chat.domain.authn.member.GuestId;
import me.jeongrae.chat.domain.authn.port.GuestIdGenerator;
import me.jeongrae.chat.domain.authn.repository.GuestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuestLoginServiceTest {

    @InjectMocks
    private GuestLoginService guestLoginService;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private GuestIdGenerator guestIdGenerator;

    @Test
    @DisplayName("게스트 로그인 성공")
    void login_Success() {
        // given
        GuestLoginCommand command = new GuestLoginCommand("guest-nickname");
        GuestId guestId = GuestId.of("test-guest-id");
        when(guestIdGenerator.generate()).thenReturn(guestId);

        // when
        guestLoginService.login(command);

        // then
        verify(guestIdGenerator, times(1)).generate();
        verify(guestRepository, times(1)).save(any(Guest.class));
    }
}
