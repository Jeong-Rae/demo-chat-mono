package me.jeongrae.chat.common.guard;

import java.util.function.Supplier;

/**
 * 숫자 유효성 검증 유틸리티입니다.
 */
public final class GuardNumbers {

    GuardNumbers() { /* no-op */ }

    private static final String NUMBER_MUST_BE_IN_RANGE = "숫자는 지정된 범위 내에 있어야 합니다.";
    private static final String NUMBER_MUST_BE_POSITIVE = "숫자는 양수여야 합니다.";
    private static final String NUMBERS_MUST_BE_EQUAL = "두 숫자는 같아야 합니다.";
    private static final String NUMBERS_MUST_NOT_BE_EQUAL = "두 숫자는 달라야 합니다.";

    /**
     * 숫자가 지정된 범위 내에 있는지 확인합니다.
     *
     * @param value   확인할 숫자
     * @param min     최소값
     * @param max     최대값
     * @param message 예외 메시지 공급자
     * @param <T>     숫자 타입
     * @return 범위 내의 숫자
     * @throws IllegalArgumentException 숫자가 범위 밖에 있는 경우
     */
    public static <T extends Number & Comparable<T>> T inRange(T value, T min, T max, Supplier<String> message) {
        Guard.notNull(value, () -> "숫자는 null일 수 없습니다.");
        Guard.notNull(min, () -> "최소값은 null일 수 없습니다.");
        Guard.notNull(max, () -> "최대값은 null일 수 없습니다.");

        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            throw new IllegalArgumentException(GuardInternal.lazy(message, NUMBER_MUST_BE_IN_RANGE));
        }
        return value;
    }

    /**
     * 숫자가 양수인지 확인합니다.
     *
     * @param value   확인할 숫자
     * @param message 예외 메시지 공급자
     * @param <T>     숫자 타입
     * @return 양수인 숫자
     * @throws IllegalArgumentException 숫자가 양수가 아닌 경우
     */
    public static <T extends Number & Comparable<T>> T positive(T value, Supplier<String> message) {
        Guard.notNull(value, () -> "숫자는 null일 수 없습니다.");
        if (value.doubleValue() <= 0) { // Assuming 0 is not positive
            throw new IllegalArgumentException(GuardInternal.lazy(message, NUMBER_MUST_BE_POSITIVE));
        }
        return value;
    }

    /**
     * 두 숫자가 같은지 확인합니다.
     *
     * @param value    확인할 숫자
     * @param expected 예상 숫자
     * @param message  예외 메시지 공급자
     * @param <T>      숫자 타입
     * @return 같은 숫자
     * @throws IllegalArgumentException 두 숫자가 같지 않은 경우
     */
    public static <T extends Number & Comparable<T>> T equals(T value, T expected, Supplier<String> message) {
        Guard.notNull(value, () -> "숫자는 null일 수 없습니다.");
        Guard.notNull(expected, () -> "예상 숫자는 null일 수 없습니다.");
        if (value.compareTo(expected) != 0) {
            throw new IllegalArgumentException(GuardInternal.lazy(message, NUMBERS_MUST_BE_EQUAL));
        }
        return value;
    }

    /**
     * 두 숫자가 다른지 확인합니다.
     *
     * @param value      확인할 숫자
     * @param unexpected 예상하지 않은 숫자
     * @param message    예외 메시지 공급자
     * @param <T>        숫자 타입
     * @return 다른 숫자
     * @throws IllegalArgumentException 두 숫자가 같은 경우
     */
    public static <T extends Number & Comparable<T>> T notEquals(T value, T unexpected, Supplier<String> message) {
        Guard.notNull(value, () -> "숫자는 null일 수 없습니다.");
        Guard.notNull(unexpected, () -> "예상하지 않은 숫자는 null일 수 없습니다.");
        if (value.compareTo(unexpected) == 0) {
            throw new IllegalArgumentException(GuardInternal.lazy(message, NUMBERS_MUST_NOT_BE_EQUAL));
        }
        return value;
    }
}
