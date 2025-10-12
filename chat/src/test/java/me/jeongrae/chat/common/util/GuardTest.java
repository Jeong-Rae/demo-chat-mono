package me.jeongrae.chat.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Guard 유틸리티 테스트")
class GuardTest {

    @Nested
    @DisplayName("notNull: null 객체 검증")
    class NotNullTest {

        @Test
        @DisplayName("null이 아니면 통과하고, 입력값을 그대로 반환한다")
        void notNull_shouldPass_whenObjectIsNotNull() {
            var obj = new Object();
            assertThat(Guard.notNull(obj)).isSameAs(obj);
        }

        @Test
        @DisplayName("null이면 NullPointerException을 던진다 (기본 메시지)")
        void notNull_shouldThrow_whenObjectIsNull() {
            assertThatThrownBy(() -> Guard.notNull(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("must not be null");
        }

        @Test
        @DisplayName("null이면 NullPointerException을 던진다 (커스텀 메시지)")
        void notNull_shouldThrow_withCustomMessage() {
            assertThatThrownBy(() -> Guard.notNull(null, () -> "custom message"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("custom message");
        }
    }

    @Nested
    @DisplayName("notBlank / hasText: 문자열 공백 검증")
    class NotBlankTest {

        @Test
        @DisplayName("정상 문자열이면 통과하고, 입력값을 그대로 반환한다")
        void notBlank_shouldPass_whenStringIsNotBlank() {
            var text = "hello";
            assertThat(Guard.notBlank(text)).isEqualTo(text);
            assertThat(Guard.hasText(text)).isEqualTo(text);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("null, 빈 문자열, 공백-only 문자열이면 IllegalArgumentException을 던진다 (기본 메시지)")
        void notBlank_shouldThrow_whenStringIsBlank(String blankText) {
            assertThatThrownBy(() -> Guard.notBlank(blankText))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("text must not be blank");

            assertThatThrownBy(() -> Guard.hasText(blankText))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("text must not be blank");
        }

        @Test
        @DisplayName("null, 빈 문자열, 공백-only 문자열이면 IllegalArgumentException을 던진다 (커스텀 메시지)")
        void notBlank_shouldThrow_withCustomMessage() {
            assertThatThrownBy(() -> Guard.notBlank(null, () -> "text is blank"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("text is blank");

            assertThatThrownBy(() -> Guard.hasText(" ", () -> "text has no text"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("text has no text");
        }
    }

    @Nested
    @DisplayName("isTrue: boolean 조건 검증")
    class IsTrueTest {

        @Test
        @DisplayName("조건이 true이면 통과한다")
        void isTrue_shouldPass_whenConditionIsTrue() {
            Guard.isTrue(true);
            Guard.isTrue(true, () -> "this should not be thrown");
        }

        @Test
        @DisplayName("조건이 false이면 IllegalArgumentException을 던진다 (기본 메시지)")
        void isTrue_shouldThrow_whenConditionIsFalse() {
            assertThatThrownBy(() -> Guard.isTrue(false))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("requirement must be true");
        }

        @Test
        @DisplayName("조건이 false이면 IllegalArgumentException을 던진다 (커스텀 메시지)")
        void isTrue_shouldThrow_withCustomMessage() {
            assertThatThrownBy(() -> Guard.isTrue(false, () -> "custom message"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("custom message");
        }
    }

    @Nested
    @DisplayName("state: 상태 검증")
    class StateTest {

        @Test
        @DisplayName("상태가 유효하면(true) 통과한다")
        void state_shouldPass_whenStateIsValid() {
            Guard.state(true);
            Guard.state(true, () -> "this should not be thrown");
        }

        @Test
        @DisplayName("상태가 유효하지 않으면(false) IllegalStateException을 던진다 (기본 메시지)")
        void state_shouldThrow_whenStateIsInvalid() {
            assertThatThrownBy(() -> Guard.state(false))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("illegal state");
        }

        @Test
        @DisplayName("상태가 유효하지 않으면(false) IllegalStateException을 던진다 (커스텀 메시지)")
        void state_shouldThrow_withCustomMessage() {
            assertThatThrownBy(() -> Guard.state(false, () -> "invalid state"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("invalid state");
        }
    }

    @Nested
    @DisplayName("unsupported: 미지원 경로 테스트")
    class UnsupportedTest {

        @Test
        @DisplayName("호출 시 항상 UnsupportedOperationException을 던진다 (기본 메시지)")
        void unsupported_shouldThrow() {
            assertThatThrownBy(() -> Guard.unsupported())
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("unsupported operation");
        }

        @Test
        @DisplayName("호출 시 항상 UnsupportedOperationException을 던진다 (커스텀 메시지)")
        void unsupported_shouldThrow_withCustomMessage() {
            assertThatThrownBy(() -> Guard.unsupported(() -> "not supported yet"))
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("not supported yet");
        }
    }

    @Nested
    @DisplayName("fail: 명시적 실패 테스트")
    class FailTest {

        @Test
        @DisplayName("호출 시 항상 IllegalArgumentException을 던진다 (기본 메시지)")
        void fail_shouldThrow() {
            assertThatThrownBy(() -> Guard.fail())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("failure");
        }

        @Test
        @DisplayName("호출 시 항상 IllegalArgumentException을 던진다 (커스텀 메시지)")
        void fail_shouldThrow_withCustomMessage() {
            assertThatThrownBy(() -> Guard.fail(() -> "explicit failure"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("explicit failure");
        }
    }
}
