package me.jeongrae.chat.common.guard;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * 프레임워크에 독립적인 가드/어설션 유틸리티입니다.
 * <p>
 * 이 유틸리티는 사전 조건 및 불변성을 확인하는 정적 메서드를 제공합니다.
 * 도메인 주도 설계(DDD) 컨텍스트에서 사용하도록 설계되었으며 외부 의존성이 없습니다.
 * <p>
 * 주요 기능:
 * <ul>
 *     <li>성능 오버헤드를 피하기 위해 {@link Supplier}를 사용하여 오류 메시지를 지연 평가합니다.</li>
 *     <li>{@code notNull} 및 {@code notBlank}와 같은 메서드에 대한 유창한 체이닝 인터페이스를 제공합니다.</li>
 *     <li>다양한 실패 시나리오에 대한 예외 유형을 명확하게 분리합니다.</li>
 * </ul>
 */
public final class Guard {
    private Guard() { /* no-op */ }

    // 예외 메시지 상수
    private static final String MUST_NOT_BE_NULL = "null일 수 없습니다.";
    private static final String TEXT_MUST_NOT_BE_BLANK = "텍스트는 공백일 수 없습니다.";
    private static final String REQUIREMENT_MUST_BE_TRUE = "조건은 참이어야 합니다.";
    private static final String ILLEGAL_STATE = "유효하지 않은 상태입니다.";
    private static final String UNSUPPORTED_OPERATION = "지원하지 않는 작업입니다.";
    private static final String FAILURE = "실패했습니다.";

    /**
     * 객체가 null이 아님을 확인합니다.
     *
     * @param value 확인할 객체
     * @param <T> 객체 타입
     * @return null이 아닌 객체
     * @throws NullPointerException 객체가 null인 경우
     */
    public static <T> T notNull(T value) {
        if (value == null) {
            throw new NullPointerException(MUST_NOT_BE_NULL);
        }
        return value;
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
            throw new NullPointerException(GuardInternal.lazy(message, MUST_NOT_BE_NULL));
        }
        return value;
    }

    /**
     * 주어진 조건이 참인지 확인합니다.
     *
     * @param condition 확인할 조건
     * @throws IllegalArgumentException 조건이 거짓인 경우
     */
    public static void isTrue(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException(REQUIREMENT_MUST_BE_TRUE);
        }
    }

    /**
     * 주어진 조건이 참인지 확인합니다. (지연 메시지)
     *
     * @param condition 확인할 조건
     * @param message 예외 메시지 공급자
     * @throws IllegalArgumentException 조건이 거짓인 경우
     */
    public static void isTrue(boolean condition, Supplier<String> message) {
        if (!condition) {
            throw new IllegalArgumentException(GuardInternal.lazy(message, REQUIREMENT_MUST_BE_TRUE));
        }
    }

    /**
     * 문자열이 null, 비어있거나 공백으로만 구성되지 않았는지 확인합니다.
     *
     * @param text 확인할 문자열
     * @return 공백이 아닌 문자열
     * @throws IllegalArgumentException 문자열이 공백인 경우
     */
    public static String notBlank(String text) {
        return GuardString.notBlank(text, () -> TEXT_MUST_NOT_BE_BLANK);
    }

    /**
     * 문자열이 null, 비어있거나 공백으로만 구성되지 않았는지 확인합니다. (지연 메시지)
     *
     * @param text 확인할 문자열
     * @param message 예외 메시지 공급자
     * @return 공백이 아닌 문자열
     * @throws IllegalArgumentException 문자열이 공백인 경우
     */
    public static String notBlank(String text, Supplier<String> message) {
        return GuardString.notBlank(text, message);
    }

    /**
     * {@link #notBlank(String)}의 별칭입니다.
     */
    public static String hasText(String text) {
        return GuardString.hasText(text, () -> TEXT_MUST_NOT_BE_BLANK);
    }

    /**
     * {@link #notBlank(String, Supplier)}의 별칭입니다.
     */
    public static String hasText(String text, Supplier<String> message) {
        return GuardString.hasText(text, message);
    }

    /**
     * 문자열의 길이가 지정된 범위 내에 있는지 확인합니다.
     *
     * @param text 확인할 문자열
     * @param minLength 최소 길이
     * @param maxLength 최대 길이
     * @return 길이 범위 내의 문자열
     * @throws IllegalArgumentException 문자열 길이가 범위를 벗어난 경우
     */
    public static String lengthBetween(String text, int minLength, int maxLength) {
        return GuardString.lengthBetween(text, minLength, maxLength, () -> "텍스트 길이가 " + minLength + "에서 " + maxLength + " 사이여야 합니다.");
    }

    /**
     * 문자열의 길이가 지정된 범위 내에 있는지 확인합니다. (지연 메시지)
     *
     * @param text 확인할 문자열
     * @param minLength 최소 길이
     * @param maxLength 최대 길이
     * @param message 예외 메시지 공급자
     * @return 길이 범위 내의 문자열
     * @throws IllegalArgumentException 문자열 길이가 범위를 벗어난 경우
     */
    public static String lengthBetween(String text, int minLength, int maxLength, Supplier<String> message) {
        return GuardString.lengthBetween(text, minLength, maxLength, message);
    }

    /**
     * 문자열이 주어진 정규식과 일치하는지 확인합니다.
     *
     * @param text 확인할 문자열
     * @param regex 정규식
     * @return 정규식과 일치하는 문자열
     * @throws IllegalArgumentException 문자열이 정규식과 일치하지 않는 경우
     */
    public static String matches(String text, String regex) {
        return GuardString.matches(text, regex, () -> "텍스트가 정규식 " + regex + "와 일치해야 합니다.");
    }

    /**
     * 문자열이 주어진 정규식과 일치하는지 확인합니다. (지연 메시지)
     *
     * @param text 확인할 문자열
     * @param regex   정규식
     * @param message 예외 메시지 공급자
     * @return 정규식과 일치하는 문자열
     * @throws IllegalArgumentException 문자열이 정규식과 일치하지 않는 경우
     */
    public static String matches(String text, String regex, Supplier<String> message) {
        return GuardString.matches(text, regex, message);
    }

    /**
     * 숫자가 지정된 범위 내에 있는지 확인합니다.
     *
     * @param value 확인할 숫자
     * @param min 최소값
     * @param max 최대값
     * @param <T> 숫자 타입
     * @return 범위 내의 숫자
     * @throws IllegalArgumentException 숫자가 범위 밖에 있는 경우
     */
    public static <T extends Number & Comparable<T>> T inRange(T value, T min, T max) {
        return GuardNumbers.inRange(value, min, max, () -> "숫자가 " + min + "에서 " + max + " 사이여야 합니다.");
    }

    /**
     * 숫자가 지정된 범위 내에 있는지 확인합니다. (지연 메시지)
     *
     * @param vlaue 확인할 숫자
     * @param min 최소값
     * @param max 최대값
     * @param message 예외 메시지 공급자
     * @param <T> 숫자 타입
     * @return 범위 내의 숫자
     * @throws IllegalArgumentException 숫자가 범위 밖에 있는 경우
     */
    public static <T extends Number & Comparable<T>> T inRange(T value, T min, T max, Supplier<String> message) {
        return GuardNumbers.inRange(value, min, max, message);
    }

    /**
     * 숫자가 양수인지 확인합니다.
     *
     * @param value 확인할 숫자
     * @param <T> 숫자 타입
     * @return 양수인 숫자
     * @throws IllegalArgumentException 숫자가 양수가 아닌 경우
     */
    public static <T extends Number & Comparable<T>> T positive(T value) {
        return GuardNumbers.positive(value, () -> "숫자는 양수여야 합니다.");
    }

    /**
     * 숫자가 양수인지 확인합니다. (지연 메시지)
     *
     * @param vlaue 확인할 숫자
     * @param message 예외 메시지 공급자
     * @param <T> 숫자 타입
     * @return 양수인 숫자
     * @throws IllegalArgumentException 숫자가 양수가 아닌 경우
     */
    public static <T extends Number & Comparable<T>> T positive(T value, Supplier<String> message) {
        return GuardNumbers.positive(value, message);
    }

    /**
     * 두 숫자가 같은지 확인합니다.
     *
     * @param vlaue 확인할 숫자
     * @param expected 예상 숫자
     * @param <T> 숫자 타입
     * @return 같은 숫자
     * @throws IllegalArgumentException 두 숫자가 같지 않은 경우
     */
    public static <T extends Number & Comparable<T>> T equals(T value, T expected) {
        return GuardNumbers.equals(value, expected, () -> "숫자 " + value + "는 " + expected + "와 같아야 합니다.");
    }

    /**
     * 두 숫자가 같은지 확인합니다. (지연 메시지)
     *
     * @param vlaue 확인할 숫자
     * @param expected 예상 숫자
     * @param message 예외 메시지 공급자
     * @param <T> 숫자 타입
     * @return 같은 숫자
     * @throws IllegalArgumentException 두 숫자가 같지 않은 경우
     */
    public static <T extends Number & Comparable<T>> T equals(T value, T expected, Supplier<String> message) {
        return GuardNumbers.equals(value, expected, message);
    }

    /**
     * 두 숫자가 다른지 확인합니다.
     *
     * @param vlaue 확인할 숫자
     * @param unexpected 예상하지 않은 숫자
     * @param <T> 숫자 타입
     * @return 다른 숫자
     * @throws IllegalArgumentException 두 숫자가 같은 경우
     */
    public static <T extends Number & Comparable<T>> T notEquals(T value, T unexpected) {
        return GuardNumbers.notEquals(value, unexpected, () -> "숫자 " + value + "는 " + unexpected + "와 달라야 합니다.");
    }

    /**
     * 두 숫자가 다른지 확인합니다. (지연 메시지)
     *
     * @param vlaue 확인할 숫자
     * @param unexpected 예상하지 않은 숫자
     * @param message 예외 메시지 공급자
     * @param <T> 숫자 타입
     * @return 다른 숫자
     * @throws IllegalArgumentException 두 숫자가 같은 경우
     */
    public static <T extends Number & Comparable<T>> T notEquals(T value, T unexpected, Supplier<String> message) {
        return GuardNumbers.notEquals(value, unexpected, message);
    }

    /**
     * 숫자가 최소값 이상인지 확인합니다.
     *
     * @param value 확인할 숫자
     * @param min 최소값
     * @param <T> 숫자 타입
     * @return 최소값 이상의 숫자
     * @throws IllegalArgumentException 숫자가 최소값 미만인 경우
     */
    public static <T extends Number & Comparable<T>> T min(T value, T min) {
        return GuardNumbers.min(value, min, () -> "숫자 " + value + "는 " + min + " 이상이어야 합니다.");
    }

    /**
     * 숫자가 최소값 이상인지 확인합니다. (지연 메시지)
     *
     * @param value 확인할 숫자
     * @param min 최소값
     * @param message 예외 메시지 공급자
     * @param <T> 숫자 타입
     * @return 최소값 이상의 숫자
     * @throws IllegalArgumentException 숫자가 최소값 미만인 경우
     */
    public static <T extends Number & Comparable<T>> T min(T value, T min, Supplier<String> message) {
        return GuardNumbers.min(value, min, message);
    }

    /**
     * 숫자가 최대값 이하인지 확인합니다.
     *
     * @param value 확인할 숫자
     * @param max 최대값
     * @param <T> 숫자 타입
     * @return 최대값 이하의 숫자
     * @throws IllegalArgumentException 숫자가 최대값 초과인 경우
     */
    public static <T extends Number & Comparable<T>> T max(T value, T max) {
        return GuardNumbers.max(value, max, () -> "숫자 " + value + "는 " + max + " 이하여야 합니다.");
    }

    /**
     * 숫자가 최대값 이하인지 확인합니다. (지연 메시지)
     *
     * @param value 확인할 숫자
     * @param max 최대값
     * @param message 예외 메시지 공급자
     * @param <T> 숫자 타입
     * @return 최대값 이하의 숫자
     * @throws IllegalArgumentException 숫자가 최대값 초과인 경우
     */
    public static <T extends Number & Comparable<T>> T max(T value, T max, Supplier<String> message) {
        return GuardNumbers.max(value, max, message);
    }


    /**
     * 컬렉션이 null이 아니고 비어있지 않은지 확인합니다.
     *
     * @param collection 확인할 컬렉션
     * @param <T> 컬렉션 타입
     * @return 비어있지 않은 컬렉션
     * @throws IllegalArgumentException 컬렉션이 null이거나 비어있는 경우
     */
    public static <T extends Collection<?>> T notEmpty(T collection) {
        return GuardCollections.notEmpty(collection, () -> "컬렉션은 비어있을 수 없습니다.");
    }

    /**
     * 컬렉션이 null이 아니고 비어있지 않은지 확인합니다. (지연 메시지)
     *
     * @param collection 확인할 컬렉션
     * @param message 예외 메시지 공급자
     * @param <T> 컬렉션 타입
     * @return 비어있지 않은 컬렉션
     * @throws IllegalArgumentException 컬렉션이 null이거나 비어있는 경우
     */
    public static <T extends Collection<?>> T notEmpty(T collection, Supplier<String> message) {
        return GuardCollections.notEmpty(collection, message);
    }

    /**
     * 컬렉션이 null 요소를 포함하지 않는지 확인합니다.
     *
     * @param collection 확인할 컬렉션
     * @param <T> 컬렉션 타입
     * @return null 요소를 포함하지 않는 컬렉션
     * @throws IllegalArgumentException 컬렉션이 null 요소를 포함하는 경우
     */
    public static <T extends Collection<?>> T noNullElements(T collection) {
        return GuardCollections.noNullElements(collection, () -> "컬렉션은 null 요소를 포함할 수 없습니다.");
    }

    /**
     * 컬렉션이 null 요소를 포함하지 않는지 확인합니다. (지연 메시지)
     *
     * @param collection 확인할 컬렉션
     * @param message 예외 메시지 공급자
     * @param <T> 컬렉션 타입
     * @return null 요소를 포함하지 않는 컬렉션
     * @throws IllegalArgumentException 컬렉션이 null 요소를 포함하는 경우
     */
    public static <T extends Collection<?>> T noNullElements(T collection, Supplier<String> message) {
        return GuardCollections.noNullElements(collection, message);
    }

    /**
     * 특정 상태가 유효한지 확인합니다.
     *
     * @param valid 확인할 조건
     * @throws IllegalStateException 상태가 유효하지 않은 경우
     */
    public static void state(boolean valid) {
        if (!valid) throw new IllegalStateException(ILLEGAL_STATE);
    }

    /**
     * 특정 상태가 유효한지 확인합니다. (지연 메시지)
     *
     * @param valid   확인할 조건
     * @param message 예외 메시지 공급자
     * @throws IllegalStateException 상태가 유효하지 않은 경우
     */
    public static void state(boolean valid, Supplier<String> message) {
        if (!valid) throw new IllegalStateException(GuardInternal.lazy(message, ILLEGAL_STATE));
    }

    /**
     * 지원하지 않는 작업임을 나타내기 위해 {@link UnsupportedOperationException}을 발생시킵니다.
     *
     * @param <T> 반환 타입
     * @return 반환되지 않음
     */
    public static <T> T unsupported() {
        throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
    }

    /**
     * 지원하지 않는 작업임을 나타내기 위해 {@link UnsupportedOperationException}을 발생시킵니다. (지연 메시지)
     *
     * @param message 예외 메시지 공급자
     * @param <T> 반환 타입
     * @return 반환되지 않음
     */
    public static <T> T unsupported(Supplier<String> message) {
        throw new UnsupportedOperationException(GuardInternal.lazy(message, UNSUPPORTED_OPERATION));
    }

    /**
     * 실패를 나타내기 위해 {@link IllegalArgumentException}을 발생시킵니다.
     *
     * @param <T> 반환 타입
     * @return 반환되지 않음
     */
    public static <T> T fail() {
        throw new IllegalArgumentException(FAILURE);
    }

    /**
     * 실패를 나타내기 위해 {@link IllegalArgumentException}을 발생시킵니다. (지연 메시지)
     *
     * @param message 예외 메시지 공급자
     * @param <T> 반환 타입
     * @return 반환되지 않음
     */
    public static <T> T fail(Supplier<String> message) {
        throw new IllegalArgumentException(GuardInternal.lazy(message, FAILURE));
    }

    /**
     * GuardCollections에 대한 접근을 제공합니다.
     *
     * @return GuardCollections 인스턴스
     */
    public static GuardCollections collections() {
        return new GuardCollections();
    }

    /**
     * GuardNumbers에 대한 접근을 제공합니다.
     *
     * @return GuardNumbers 인스턴스
     */
    public static GuardNumbers numbers() {
        return new GuardNumbers();
    }

    /**
     * GuardString에 대한 접근을 제공합니다.
     *
     * @return GuardString 인스턴스
     */
    public static GuardString strings() {
        return new GuardString();
    }
}