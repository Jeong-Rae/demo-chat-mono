package me.jeongrae.chat.domain.authn.member;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.authn.policy.CredentialPolicy;
import me.jeongrae.chat.domain.authn.policy.PasswordHasher;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;
import me.jeongrae.chat.domain.shared.model.Entity;

/**
 * 인증 컨텍스트의 Member 애그리게이트 루트.
 */
@Getter
@Accessors(fluent = true)
public class Member extends Entity<MemberId> {

        private final String username;
        private final String nickname;
        private final HashedPassword hashedPassword;

        private Member(MemberId memberId, String username, String nickname,
                        HashedPassword hashedPassword) {
                super(memberId);
                this.username = Guard.notBlank(username,
                                ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("username"));
                this.nickname = Guard.notBlank(nickname,
                                ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("nickname"));
                this.hashedPassword = Guard.notNull(hashedPassword,
                                ErrorTemplate.VALUE_CANNOT_BE_NULL.format("hashedPassword"));
        }

        public static Member register(MemberId memberId, String username, String nickname,
                        Password password, CredentialPolicy credentialPolicy,
                        PasswordHasher passwordHasher) {
                credentialPolicy.check(username, nickname, password);

                HashedPassword hashedPassword = passwordHasher.hash(password);

                return new Member(memberId, username, nickname, hashedPassword);
        }

        public static Member of(MemberId memberId, String username, String nickname,
                        HashedPassword hashedPassword) {
                return new Member(memberId, username, nickname, hashedPassword);
        }

        public boolean authenticate(Password password, PasswordHasher passwordHasher) {
                Password validatedPassword = Guard.notNull(password,
                                ErrorTemplate.VALUE_CANNOT_BE_NULL.format("password"));
                PasswordHasher validatedHasher = Guard.notNull(passwordHasher,
                                ErrorTemplate.VALUE_CANNOT_BE_NULL.format("passwordHasher"));
                return validatedHasher.matches(validatedPassword, hashedPassword);
        }
}
