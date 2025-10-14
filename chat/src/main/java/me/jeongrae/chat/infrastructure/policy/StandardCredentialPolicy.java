package me.jeongrae.chat.infrastructure.policy;

import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.authn.credential.Password;
import me.jeongrae.chat.domain.authn.credential.PasswordStrength;
import me.jeongrae.chat.domain.authn.policy.CredentialPolicy;
import me.jeongrae.chat.domain.shared.error.ChatErrorCode;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class StandardCredentialPolicy implements CredentialPolicy {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9]{4,20}$");
    private static final PasswordStrength REQUIRED_PASSWORD_STRENGTH = PasswordStrength.MEDIUM;

    @Override
    public void check(String username, String nickname, Password password) {
        Guard.notBlank(username, ChatErrorCode.USERNAME_CANNOT_BE_BLANK.defaultMessage());
        Guard.isTrue(USERNAME_PATTERN.matcher(username).matches(),
                ChatErrorCode.INVALID_USERNAME_FORMAT.defaultMessage());

        Guard.notBlank(nickname, ChatErrorCode.NICKNAME_CANNOT_BE_BLANK.defaultMessage());

        Guard.notNull(password, ChatErrorCode.PASSWORD_CANNOT_BE_NULL.defaultMessage());
        PasswordStrength strength = password.strength();
        Guard.isTrue(strength.meetsOrExceeds(REQUIRED_PASSWORD_STRENGTH),
                ChatErrorCode.PASSWORD_TOO_WEAK.defaultMessage());
    }
}
