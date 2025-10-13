package me.jeongrae.chat.common.guard;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * 컬렉션 유효성 검증 유틸리티입니다.
 */
public final class GuardCollections {

    GuardCollections() { /* no-op */ }

    private static final String COLLECTION_MUST_NOT_BE_EMPTY = "컬렉션은 비어있을 수 없습니다.";
    private static final String COLLECTION_MUST_NOT_CONTAIN_NULL_ELEMENTS = "컬렉션은 null 요소를 포함할 수 없습니다.";

    /**
     * 컬렉션이 null이 아니고 비어있지 않은지 확인합니다.
     *
     * @param collection 확인할 컬렉션
     * @param message    예외 메시지 공급자
     * @param <T>        컬렉션 타입
     * @return 비어있지 않은 컬렉션
     * @throws IllegalArgumentException 컬렉션이 null이거나 비어있는 경우
     */
    public static <T extends Collection<?>> T notEmpty(T collection, Supplier<String> message) {
        GuardInternal.notNull(collection, () -> "컬렉션은 null일 수 없습니다.");
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(GuardInternal.lazy(message, COLLECTION_MUST_NOT_BE_EMPTY));
        }
        return collection;
    }

    /**
     * 컬렉션이 null 요소를 포함하지 않는지 확인합니다.
     *
     * @param collection 확인할 컬렉션
     * @param message    예외 메시지 공급자
     * @param <T>        컬렉션 타입
     * @return null 요소를 포함하지 않는 컬렉션
     * @throws IllegalArgumentException 컬렉션이 null 요소를 포함하는 경우
     */
    public static <T extends Collection<?>> T noNullElements(T collection, Supplier<String> message) {
        GuardInternal.notNull(collection, () -> "컬렉션은 null일 수 없습니다.");
        for (Object element : collection) {
            if (element == null) {
                throw new IllegalArgumentException(GuardInternal.lazy(message, COLLECTION_MUST_NOT_CONTAIN_NULL_ELEMENTS));
            }
        }
        return collection;
    }
}
