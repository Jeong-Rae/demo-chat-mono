package me.jeongrae.chat.config;

import lombok.RequiredArgsConstructor;
import me.jeongrae.chat.infrastructure.security.MemberDetailsService;
import me.jeongrae.chat.infrastructure.security.GuestAuthenticationProvider;
import me.jeongrae.chat.interfaces.web.GuestAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final MemberDetailsService memberDetailsService;
        private final GuestAuthenticationProvider guestAuthenticationProvider;

        @Bean
        public PasswordEncoder passwordEncoder() {
                int saltLenth = 16; // 2^4;
                int hashLenth = 32; // 2^5;
                int parallelism = 1;
                int memory = 65536; // 2^16;
                int iterations = 10;
                return new Argon2PasswordEncoder(saltLenth, hashLenth, parallelism, memory,
                                iterations);
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authBuilder =
                                http.getSharedObject(AuthenticationManagerBuilder.class);
                authBuilder.userDetailsService(memberDetailsService)
                                .passwordEncoder(passwordEncoder());
                authBuilder.authenticationProvider(guestAuthenticationProvider);
                return authBuilder.build();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http,
                        AuthenticationManager authenticationManager) throws Exception {
                http.csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(authz -> authz
                                                .requestMatchers("/api/register/member",
                                                                "/api/login/guest", "/login")
                                                .permitAll().anyRequest().authenticated())
                                .formLogin(form -> form.loginProcessingUrl("/api/login/member")
                                                .permitAll())
                                .logout(logout -> logout.permitAll());

                GuestAuthenticationFilter guestAuthenticationFilter =
                                new GuestAuthenticationFilter(authenticationManager);
                http.addFilterBefore(guestAuthenticationFilter,
                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
