package me.jeongrae.chat.domain.shared.error;

import lombok.Builder;
import lombok.Singular;

import java.util.Map;

@Builder
public final class ErrorDetails {

    @Singular("value")
    private final Map<String, Object> values;

    public static ErrorDetails empty() {
        return ErrorDetails.builder().build();
    }

    public static ErrorDetails of(Map<String, Object> map) {
        return ErrorDetails.builder().values(map).build();
    }

    /**
     * key-value 쌍으로부터 ErrorDetails를 생성합니다.
     * 
     * @param kvPairs key, value, key, value ... 형식의 배열
     * @return 생성된 ErrorDetails 객체
     */
    public static ErrorDetails of(Object... kvPairs) {
        if (kvPairs == null || kvPairs.length == 0) {
            return empty();
        }
        if (kvPairs.length % 2 != 0) {
            throw new IllegalArgumentException("key-value 쌍은 짝수여야 합니다.");
        }
        var builder = ErrorDetails.builder();
        for (int i = 0; i < kvPairs.length; i += 2) {
            builder.value(String.valueOf(kvPairs[i]), kvPairs[i + 1]);
        }
        return builder.build();
    }

    public Map<String, Object> asMap() {
        return this.values;
    }
}
