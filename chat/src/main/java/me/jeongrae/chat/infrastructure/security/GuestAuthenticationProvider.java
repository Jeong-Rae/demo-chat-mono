package me.jeongrae.chat.infrastructure.security;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.application.GuestLoginCommand;
import me.jeongrae.chat.application.service.GuestLoginService;
import me.jeongrae.chat.domain.authn.member.Guest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class GuestAuthenticationProvider implements AuthenticationProvider {

    private final GuestLoginService guestLoginService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String nickname = (String) authentication.getPrincipal();
        GuestLoginCommand command = new GuestLoginCommand(nickname);

        // Use the application service to create and save the guest
        Guest guest = guestLoginService.login(command);

        // Return a fully authenticated token
        return new GuestAuthenticationToken(guest, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return GuestAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
