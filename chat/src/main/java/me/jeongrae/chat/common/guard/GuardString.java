package me.jeongrae.chat.common.guard;

import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * 문자열 유효성 검증 유틸리티입니다.
 */
public final class GuardString {

    GuardString() { /* no-op */ }

    private static final String TEXT_MUST_NOT_BE_BLANK = "텍스트는 공백일 수 없습니다.";
    private static final String TEXT_LENGTH_OUT_OF_RANGE = "텍스트 길이가 범위를 벗어났습니다.";
    private static final String TEXT_DOES_NOT_MATCH_REGEX = "텍스트가 정규식과 일치하지 않습니다.";

    /**
     * 문자열이 null, 비어있거나 공백으로만 구성되지 않았는지 확인합니다.
     *
     * @param text 확인할 문자열
     * @param message 예외 메시지 공급자
     * @return 공백이 아닌 문자열
     * @throws IllegalArgumentException 문자열이 공백인 경우
     */
    public static String notBlank(String text, Supplier<String> message) {
        return notBlank(text, GuardInternal.lazy(message, TEXT_MUST_NOT_BE_BLANK));
    }

    /**
     * 문자열이 null, 비어있거나 공백으로만 구성되지 않았는지 확인합니다.
     *
     * @param text 확인할 문자열
     * @param message 예외 메시지
     * @return 공백이 아닌 문자열
     * @throws IllegalArgumentException 문자열이 공백인 경우
     */
    public static String notBlank(String text, String message) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return text;
    }

    /**
     * {@link #notBlank(String, Supplier)}의 별칭입니다.
     */
    public static String hasText(String text, Supplier<String> message) {
        return notBlank(text, message);
    }

    /**
     * {@link #notBlank(String, String)}의 별칭입니다.
     */
    public static String hasText(String text, String message) {
        return notBlank(text, message);
    }

    /**
     * 문자열의 길이가 지정된 범위 내에 있는지 확인합니다.
     *
     * @param text 확인할 문자열
     * @param minLength 최소 길이
     * @param maxLength 최대 길이
     * @param message 예외 메시지 공급자
     * @return 길이 범위 내의 문자열
     * @throws IllegalArgumentException 문자열 길이가 범위를 벗어난 경우
     */
    public static String lengthBetween(String text, int minLength, int maxLength, Supplier<String> message) {
        return lengthBetween(text, minLength, maxLength, GuardInternal.lazy(message, TEXT_LENGTH_OUT_OF_RANGE));
    }

    /**
     * 문자열의 길이가 지정된 범위 내에 있는지 확인합니다.
     *
     * @param text 확인할 문자열
     * @param minLength 최소 길이
     * @param maxLength 최대 길이
     * @param message 예외 메시지
     * @return 길이 범위 내의 문자열
     * @throws IllegalArgumentException 문자열 길이가 범위를 벗어난 경우
     */
    public static String lengthBetween(String text, int minLength, int maxLength, String message) {
        GuardInternal.notNull(text, message);
        int length = text.length();
        if (length < minLength || length > maxLength) {
            throw new IllegalArgumentException(message);
        }
        return text;
    }

    /**
     * 문자열이 주어진 정규식과 일치하는지 확인합니다.
     *
     * @param text 확인할 문자열
     * @param regex 정규식
     * @param message 예외 메시지 공급자
     * @return 정규식과 일치하는 문자열
     * @throws IllegalArgumentException 문자열이 정규식과 일치하지 않는 경우
     */
    public static String matches(String text, String regex, Supplier<String> message) {
        return matches(text, regex, GuardInternal.lazy(message, TEXT_DOES_NOT_MATCH_REGEX));
    }

    /**
     * 문자열이 주어진 정규식과 일치하는지 확인합니다.
     *
     * @param text 확인할 문자열
     * @param regex 정규식
     * @param message 예외 메시지
     * @return 정규식과 일치하는 문자열
     * @throws IllegalArgumentException 문자열이 정규식과 일치하지 않는 경우
     */
    public static String matches(String text, String regex, String message) {
        GuardInternal.notNull(text, message);
        GuardInternal.notNull(regex, message);
        if (!Pattern.matches(regex, text)) {
            throw new IllegalArgumentException(message);
        }
        return text;
    }
}
