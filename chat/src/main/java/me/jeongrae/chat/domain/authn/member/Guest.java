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
 * 인증 컨텍스트의 Guest 애그리게이트 루트.
 */
@Getter
@Accessors(fluent = true)
public class Guest extends Entity<GuestId> {

    private static final PasswordStrength REQUIRED_PASSWORD_STRENGTH = PasswordStrength.WEAK;

    private final String username;
    private final HashedPassword hashedPassword;

    private Guest(GuestId guestId, String username, HashedPassword hashedPassword) {
        super(guestId);
        this.username = Guard.notBlank(username, ErrorTemplate.VALUE_CANNOT_BE_EMPTY.format("username"));
        this.hashedPassword = Guard.notNull(hashedPassword, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("hashedPassword"));
    }

    public static Guest register(GuestId guestId, String username, Password password, PasswordPolicy passwordPolicy) {
        Password validatedPassword = Guard.notNull(password, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("password"));
        PasswordPolicy validatedPolicy = Guard.notNull(passwordPolicy, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("passwordPolicy"));
        PasswordStrength strength = validatedPassword.strength();
        Guard.isTrue(strength.meetsOrExceeds(REQUIRED_PASSWORD_STRENGTH),
            ErrorTemplate.PASSWORD_STRENGTH_REQUIREMENT.format("Guest"));
        HashedPassword hashedPassword = Guard.notNull(validatedPolicy.hash(validatedPassword),
            ErrorTemplate.VALUE_CANNOT_BE_NULL.format("hashedPassword"));
        return new Guest(guestId, username, hashedPassword);
    }

    public boolean authenticate(Password password, PasswordPolicy passwordPolicy) {
        Password validatedPassword = Guard.notNull(password, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("password"));
        PasswordPolicy validatedPolicy = Guard.notNull(passwordPolicy, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("passwordPolicy"));
        return validatedPolicy.matches(validatedPassword, hashedPassword);
    }
}
