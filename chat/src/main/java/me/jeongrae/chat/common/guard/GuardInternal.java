package me.jeongrae.chat.common.guard;

import java.util.function.Supplier;

/**
 * Guard 유틸리티를 위한 내부 헬퍼입니다.
 * <p>
 * 이 클래스는 외부 사용을 위한 것이 아닙니다.
 */
public final class GuardInternal {

    private static final String MUST_NOT_BE_NULL = "null일 수 없습니다.";

    private GuardInternal() { /* no-op */ }

    /**
     * Supplier로부터 메시지를 지연 평가합니다.
     *
     * @param supplier 메시지 공급자
     * @param fallback supplier가 null이거나 실패할 경우 대체 메시지
     * @return 평가된 메시지 또는 대체 메시지
     */
    public static String lazy(Supplier<String> supplier, String fallback) {
        try {
            return supplier != null ? String.valueOf(supplier.get()) : fallback;
        } catch (Exception ignored) {
            return fallback;
        }
    }

    /**
     * 객체가 null이 아님을 확인합니다. (지연 메시지)
     *
     * @param value 확인할 객체
     * @param message 예외 메시지 공급자
     * @param <T> 객체 타입
     * @return null이 아닌 객체
     * @throws NullPointerException 객체가 null인 경우
     */
    public static <T> T notNull(T value, Supplier<String> message) {
        if (value == null) {
            throw new NullPointerException(lazy(message, MUST_NOT_BE_NULL));
        }
        return value;
    }

    /**
     * 객체가 null이 아님을 확인합니다.
     *
     * @param value 확인할 객체
     * @param message 예외 메시지
     * @param <T> 객체 타입
     * @return null이 아닌 객체
     * @throws NullPointerException 객체가 null인 경우
     */
    public static <T> T notNull(T value, String message) {
        return notNull(value, () -> message);
    }
}
