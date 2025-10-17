# Guard Usage Guide

`Guard`는 DDD의 Domain 계층에서 사용하기 적합하도록 외부 의존성 없이 설계된 유틸리티입니다.
메서드 체이닝을 지원하고, `Supplier<String>`를 통해 예외 메시지를 지연 평가하여 성능을 최적화합니다.

## 목차

1.  [기본 원칙](#1-기본-원칙)
2.  [Object & General](#2-object--general)
3.  [String](#3-string)
4.  [Number](#4-number)
5.  [Collection](#5-collection)

---

## 1. 기본 원칙

- **메시지 지연 평가**: 예외 메시지는 `Supplier<String>`를 사용하여 실제 예외가 발생할 때만 생성됩니다.
- **예외 타입 분리**:
    - `NullPointerException`: `null` 값 검증 실패
    - `IllegalArgumentException`: 메서드 인자 검증 실패
    - `IllegalStateException`: 객체 상태 검증 실패
    - `UnsupportedOperationException`: 지원되지 않는 기능 호출
- **메서드 체이닝**: 검증 성공 시 입력값을 반환하여 연속적인 호출 및 할당을 지원합니다. (`isTrue`, `state` 등 제외)

---

## 2. Object & General

### `notNull`
값이 `null`인지 확인합니다.

```java
var v1 = Guard.notNull("str", "v1은 null일 수 없습니다."); // -> "str"
var v2 = Guard.notNull(null, () -> "v2는 null이어야 합니다."); // throws NullPointerException
```

### `isTrue`
조건이 `true`인지 확인합니다.

```java
Guard.isTrue(1 < 2, "1은 2보다 작아야 합니다."); // OK
Guard.isTrue(1 > 2, () -> "1은 2보다 클 수 없습니다."); // throws IllegalArgumentException
```

### `equals`
두 객체가 동일한지 확인합니다.

```java
var v1 = Guard.equals("a", "a", "두 객체는 동일해야 합니다."); // -> "a"
var v2 = Guard.equals("a", "b", () -> "두 객체는 동일해야 합니다."); // throws IllegalArgumentException
```

### `notEquals`
두 객체가 다른지 확인합니다.

```java
var v1 = Guard.notEquals("a", "b", "두 객체는 달라야 합니다."); // -> "a"
var v2 = Guard.notEquals("a", "a", () -> "두 객체는 달라야 합니다."); // throws IllegalArgumentException
```

### `state`
객체의 상태가 유효한지 확인합니다.

```java
Guard.state(true, "상태가 유효해야 합니다."); // OK
Guard.state(false, () -> "상태가 유효하지 않습니다."); // throws IllegalStateException
```

### `fail`
의도적으로 `IllegalArgumentException`을 발생시킵니다.

```java
Guard.fail("이 코드는 실행되면 안됩니다."); // throws IllegalArgumentException
```

### `unsupported`
지원되지 않는 연산임을 알리기 위해 `UnsupportedOperationException`을 발생시킵니다.

```java
Guard.unsupported("이 기능은 아직 구현되지 않았습니다."); // throws UnsupportedOperationException
```

---

## 3. String

### `notBlank` / `hasText`
문자열이 `null`이 아니고, 공백 문자로만 이루어져 있지 않은지 확인합니다.

```java
var v1 = Guard.notBlank(" text ", "v1은 비어있을 수 없습니다."); // -> " text "
var v2 = Guard.hasText(" ", () -> "v2는 비어있을 수 없습니다."); // throws IllegalArgumentException
```

### `lengthBetween`
문자열의 길이가 지정된 범위 내에 있는지 확인합니다.

```java
var v1 = Guard.lengthBetween("abc", 1, 3, "길이는 1과 3 사이여야 합니다."); // -> "abc"
var v2 = Guard.lengthBetween("abcd", 1, 3, () -> "길이는 1과 3 사이여야 합니다."); // throws IllegalArgumentException
```

### `matches`
문자열이 정규식과 일치하는지 확인합니다.

```java
var v1 = Guard.matches("abc", "^[a-z]+$", "알파벳 소문자만 허용됩니다."); // -> "abc"
var v2 = Guard.matches("a1", "^[a-z]+$", () -> "알파벳 소문자만 허용됩니다."); // throws IllegalArgumentException
```

---

## 4. Number

### `inRange`
숫자가 지정된 범위 내에 있는지 확인합니다. (`min` <= `value` <= `max`)

```java
var v1 = Guard.inRange(5, 1, 10, "값은 1과 10 사이여야 합니다."); // -> 5
var v2 = Guard.inRange(0, 1, 10, () -> "값은 1과 10 사이여야 합니다."); // throws IllegalArgumentException
```

### `positive`
숫자가 양수인지 확인합니다. (`value` > 0)

```java
var v1 = Guard.positive(1, "값은 양수여야 합니다."); // -> 1
var v2 = Guard.positive(0, () -> "값은 양수여야 합니다."); // throws IllegalArgumentException
```

### `min`
숫자가 최소값보다 크거나 같은지 확인합니다. (`value` >= `min`)

```java
var v1 = Guard.min(5, 5, "값은 5 이상이어야 합니다."); // -> 5
var v2 = Guard.min(4, 5, () -> "값은 5 이상이어야 합니다."); // throws IllegalArgumentException
```

### `max`
숫자가 최대값보다 작거나 같은지 확인합니다. (`value` <= `max`)

```java
var v1 = Guard.max(5, 5, "값은 5 이하여야 합니다."); // -> 5
var v2 = Guard.max(6, 5, () -> "값은 5 이하여야 합니다."); // throws IllegalArgumentException
```

---

## 5. Collection

### `notEmpty`
컬렉션이 `null`이 아니고, 비어있지 않은지 확인합니다.

```java
var v1 = Guard.notEmpty(List.of(1), "컬렉션은 비어있을 수 없습니다."); // -> [1]
var v2 = Guard.notEmpty(List.of(), () -> "컬렉션은 비어있을 수 없습니다."); // throws IllegalArgumentException
```

### `noNullElements`
컬렉션 내에 `null` 요소가 없는지 확인합니다.

```java
var v1 = Guard.noNullElements(List.of(1, 2), "null 요소를 포함할 수 없습니다."); // -> [1, 2]
var v2 = Guard.noNullElements(Arrays.asList(1, null), () -> "null 요소를 포함할 수 없습니다."); // throws IllegalArgumentException
```
