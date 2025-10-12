package me.jeongrae.chat.common.util;

import java.util.function.Supplier;

/**
 * Framework-agnostic guard/assert utility.
 * - 메시지 제공 시 반드시 Supplier<String>을 사용해 지연평가.
 * - 반환형은 가능하면 입력값을 그대로 반환하여 한 줄 체이닝을 지원.
 *
 * Java 17, DDD 도메인 계층에서 사용하기 적합하도록 외부 의존성 없음.
 */
public final class Guard {
    private Guard() { /* no-op */ }

    /* -------------------------------------------
     * null check
     * ------------------------------------------- */

    /** value가 null이면 NullPointerException */
    public static <T> T notNull(T value) {
        if (value == null) throw new NullPointerException("must not be null");
        return value;
    }

    /** value가 null이면 NullPointerException (지연 메시지) */
    public static <T> T notNull(T value, Supplier<String> message) {
        if (value == null) throw new NullPointerException(lazy(message, "must not be null"));
        return value;
    }

    /* -------------------------------------------
     * text check: notBlank / hasText
     * - notBlank: null/빈문자열/공백만으로 구성 → 실패
     * - hasText : 같은 의미로 제공 (선호하는 네이밍 선택)
     * ------------------------------------------- */

    /** text가 null, 빈 문자열, 공백-only면 IllegalArgumentException */
    public static String notBlank(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("text must not be blank");
        }
        return text;
    }

    /** text가 null, 빈 문자열, 공백-only면 IllegalArgumentException (지연 메시지) */
    public static String notBlank(String text, Supplier<String> message) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(lazy(message, "text must not be blank"));
        }
        return text;
    }

    /** hasText = notBlank와 동일 의미 제공(팀 선호 네이밍을 사용하세요) */
    public static String hasText(String text) {
        return notBlank(text);
    }

    public static String hasText(String text, Supplier<String> message) {
        return notBlank(text, message);
    }

    /* -------------------------------------------
     * boolean check (argument / state)
     * ------------------------------------------- */

    /** 조건이 false면 IllegalArgumentException */
    public static void isTrue(boolean condition) {
        if (!condition) throw new IllegalArgumentException("requirement must be true");
    }

    /** 조건이 false면 IllegalArgumentException (지연 메시지) */
    public static void isTrue(boolean condition, Supplier<String> message) {
        if (!condition) throw new IllegalArgumentException(lazy(message, "requirement must be true"));
    }

    /** 상태가 유효하지 않으면 IllegalStateException */
    public static void state(boolean valid) {
        if (!valid) throw new IllegalStateException("illegal state");
    }

    /** 상태가 유효하지 않으면 IllegalStateException (지연 메시지) */
    public static void state(boolean valid, Supplier<String> message) {
        if (!valid) throw new IllegalStateException(lazy(message, "illegal state"));
    }

    /* -------------------------------------------
     * unsupported / fail helpers
     * ------------------------------------------- */

    /** 미지원 경로: 항상 UnsupportedOperationException */
    public static <T> T unsupported() {
        throw new UnsupportedOperationException("unsupported operation");
    }

    /** 미지원 경로: 항상 UnsupportedOperationException (지연 메시지) */
    public static <T> T unsupported(Supplier<String> message) {
        throw new UnsupportedOperationException(lazy(message, "unsupported operation"));
    }

    /** 명시적 실패(입력 오류 등): IllegalArgumentException */
    public static <T> T fail() {
        throw new IllegalArgumentException("failure");
    }

    /** 명시적 실패(입력 오류 등): IllegalArgumentException (지연 메시지) */
    public static <T> T fail(Supplier<String> message) {
        throw new IllegalArgumentException(lazy(message, "failure"));
    }

    /* -------------------------------------------
     * internal
     * ------------------------------------------- */

    private static String lazy(Supplier<String> supplier, String fallback) {
        try {
            return supplier != null ? String.valueOf(supplier.get()) : fallback;
        } catch (Exception ignored) {
            return fallback;
        }
    }
}
