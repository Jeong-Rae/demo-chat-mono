package me.jeongrae.chat.domain.authn.credential;

import java.util.regex.Pattern;
import me.jeongrae.chat.common.guard.Guard;
import me.jeongrae.chat.domain.shared.error.ErrorTemplate;

/**
 * 비밀번호 강도를 표현한다.
 */
public enum PasswordStrength {
    WEAK,
    MEDIUM,
    STRONG;

    private static final Pattern ALLOWED_CHARACTERS = Pattern.compile("^[\\p{Alnum}\\p{Punct}]+$");
    private static final Pattern LETTER_PATTERN = Pattern.compile("[A-Za-z]");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");
    private static final Pattern SPECIAL_CHARACTER_PATTERN = Pattern.compile("[\\p{Punct}]");

    public static PasswordStrength evaluate(Password password) {
        Password validatedPassword = Guard.notNull(password, ErrorTemplate.VALUE_CANNOT_BE_NULL.format("password"));
        String value = validatedPassword.value();

        Guard.isTrue(ALLOWED_CHARACTERS.matcher(value).matches(),
            ErrorTemplate.PASSWORD_STRENGTH_REQUIREMENT.format("password"));

        if (isStrong(value)) {
            return STRONG;
        }
        if (isMedium(value)) {
            return MEDIUM;
        }
        if (isWeak(value)) {
            return WEAK;
        }

        throw new IllegalArgumentException(ErrorTemplate.PASSWORD_STRENGTH_REQUIREMENT.format("password"));
    }

    public boolean meetsOrExceeds(PasswordStrength requirement) {
        return this.ordinal() >= requirement.ordinal();
    }

    private static boolean isStrong(String value) {
        return value.length() >= 8
            && LETTER_PATTERN.matcher(value).find()
            && DIGIT_PATTERN.matcher(value).find()
            && SPECIAL_CHARACTER_PATTERN.matcher(value).find()
            && UPPERCASE_PATTERN.matcher(value).find();
    }

    private static boolean isMedium(String value) {
        return value.length() >= 8
            && LETTER_PATTERN.matcher(value).find()
            && DIGIT_PATTERN.matcher(value).find()
            && SPECIAL_CHARACTER_PATTERN.matcher(value).find();
    }

    private static boolean isWeak(String value) {
        return value.length() >= 4;
    }
}
