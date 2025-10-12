package me.jeongrae.chat.common.guard;

import java.util.function.Supplier;

/**
 * Guard 유틸리티를 위한 내부 헬퍼입니다.
 * <p>
 * 이 클래스는 외부 사용을 위한 것이 아닙니다.
 */
public final class GuardInternal {

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
}
