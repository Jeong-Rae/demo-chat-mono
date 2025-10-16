package me.jeongrae.chat.interfaces.web;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.application.GuestLoginCommand;
import me.jeongrae.chat.application.MemberLoginCommand;
import me.jeongrae.chat.application.service.AuthenticationService;
import me.jeongrae.chat.application.service.TokenRefreshService;
import me.jeongrae.chat.interfaces.dto.GuestLoginRequest;
import me.jeongrae.chat.interfaces.dto.MemberLoginRequest;
import me.jeongrae.chat.interfaces.dto.TokenRefreshRequest;
import me.jeongrae.chat.interfaces.dto.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenRefreshService tokenRefreshService;

    @PostMapping("/login/member")
    public ResponseEntity<TokenResponse> loginMember(@RequestBody MemberLoginRequest request) {
        MemberLoginCommand command = new MemberLoginCommand(request.username(), request.password());
        TokenResponse tokens = authenticationService.loginMember(command);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/login/guest")
    public ResponseEntity<TokenResponse> loginGuest(@RequestBody GuestLoginRequest request) {
        GuestLoginCommand command = new GuestLoginCommand(request.nickname());
        TokenResponse token = authenticationService.loginGuest(command);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshTokens(@RequestBody TokenRefreshRequest request) {
        TokenResponse newTokens = tokenRefreshService.refreshTokens(request.refreshToken());
        return ResponseEntity.ok(newTokens);
    }
}
