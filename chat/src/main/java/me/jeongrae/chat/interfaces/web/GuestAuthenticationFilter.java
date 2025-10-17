package me.jeongrae.chat.interfaces.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.jeongrae.chat.infrastructure.security.GuestAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

public class GuestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public GuestAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/api/login/guest", "POST"));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String nickname = request.getParameter("nickname");
        if (nickname == null) {
            nickname = "";
        }

        GuestAuthenticationToken authRequest = new GuestAuthenticationToken(nickname.strip());
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        // The SecurityContextPersistenceFilter will save the context, creating the session.
        // We don't need to do anything else here.
        // You might want to return a success response to the client.
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
