package me.jeongrae.chat.domain.authn.member;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.authn.credential.HashedPassword;
import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.authn.credential.PasswordStrength;
import me.jeongrae.chat.domain.authn.policy.PasswordPolicy;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;
import me.jeongrae.chat.domain.shared.model.Entity;

/**
 * 인증 컨텍스트의 Member 애그리게이트 루트.
 */
@Getter
@Accessors(fluent = true)
public class Member extends Entity<MemberId> {

    private static final PasswordStrength REQUIRED_PASSWORD_STRENGTH = PasswordStrength.MEDIUM;

    private final String username;
    private final String nickname;
    private final HashedPassword hashedPassword;

    private Member(MemberId memberId, String username, String nickname,
            HashedPassword hashedPassword) {
        super(memberId);
        this.username =
                Guard.notBlank(username, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("username"));
        this.nickname =
                Guard.notBlank(nickname, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("nickname"));
        this.hashedPassword = Guard.notNull(hashedPassword,
                ErrorTemplate.VALUE_CANNOT_BE_NULL.format("hashedPassword"));
    }

    public static Member register(MemberId memberId, String username, String nickname,
            Password password, PasswordPolicy passwordPolicy) {
        Password validatedPassword =
                Guard.notNull(password, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("password"));
        PasswordPolicy validatedPolicy = Guard.notNull(passwordPolicy,
                ErrorTemplate.VALUE_CANNOT_BE_NULL.format("passwordPolicy"));
        PasswordStrength strength = validatedPassword.strength();
        Guard.isTrue(strength.meetsOrExceeds(REQUIRED_PASSWORD_STRENGTH),
                ErrorTemplate.PASSWORD_STRENGTH_REQUIREMENT.format("Member"));
        HashedPassword hashedPassword = Guard.notNull(validatedPolicy.hash(validatedPassword),
                ErrorTemplate.VALUE_CANNOT_BE_NULL.format("hashedPassword"));

        return new Member(memberId, username, nickname, hashedPassword);
    }

    public boolean authenticate(Password password, PasswordPolicy passwordPolicy) {
        Password validatedPassword =
                Guard.notNull(password, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("password"));
        PasswordPolicy validatedPolicy = Guard.notNull(passwordPolicy,
                ErrorTemplate.VALUE_CANNOT_BE_NULL.format("passwordPolicy"));
                
        return validatedPolicy.matches(validatedPassword, hashedPassword);
    }
}
