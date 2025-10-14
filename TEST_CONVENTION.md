# Test Convention

This document defines the Test-Driven Development (TDD) process and related conventions for this project. All new domain models, services, and utility classes must adhere to these guidelines.

## 1. Kent Beck's TDD Cycle

Development must follow the "Red-Green-Refactor" cycle:

1.  **RED**: Write a small, failing test that defines a new function or improvement. The test should fail because the required code does not yet exist.
2.  **GREEN**: Write the simplest possible production code to make the test pass. Avoid writing code that is not necessary to pass the test.
3.  **REFACTOR**: Clean up the code you've just written. Refactor to improve readability, remove duplication, and enhance design, all while ensuring that all tests remain green.

This cycle must be applied for every new piece of functionality.

## 2. Development Workflow

**Always write a test before writing implementation code.**

After implementing the code to pass the test, **run the individual test file directly** from the IDE to verify the changes. This provides rapid feedback without waiting for a full project build (`build`). The cycle is complete only after the test passes and the code has been refactored.

## 3. Test Code Conventions

### Test Method Naming

Test method names should follow the `feature_shouldExpectedBehavior_whenCondition` pattern. This makes it clear what each test is verifying.

-   **feature**: The name of the method or feature being tested.
-   **shouldExpectedBehavior**: The expected outcome or behavior.
-   **whenCondition**: The specific condition under which the behavior is expected.

**Example:**
```java
@Test
void notEmpty_shouldReturnValue_whenCollectionIsNotEmpty() { ... }

@Test
void notEmpty_shouldThrowException_whenCollectionIsEmpty() { ... }
```

### `@DisplayName` Annotation
All test classes and methods **must** use the `@DisplayName` annotation to provide a clear, human-readable description in **Korean**.
-   **Class-level**: Describes the component being tested.
-   **Method-level**: Describes the specific scenario being tested.



**Example:**

```java

@DisplayName("GuardCollections 유틸리티 테스트")

class GuardCollectionsTest {
    @DisplayName("notEmpty: 컬렉션이 비어있지 않은 경우 값을 반환한다")
    @Test
    void notEmpty_shouldReturnValue_whenNotEmpty() {
        // ...
    }

}

```

### Test Structure: Given-When-Then
The body of each test method must be structured to clearly separate the phases of the test.

#### Success Case: Separating `Given`, `When`, `Then`
In a typical success case, each phase is clearly separated to enhance readability.
-   **`// Given`**: Set up the necessary state and inputs for the test.
-   **`// When`**: Execute the method being tested.
-   **`// Then`**: Assert the outcome of the execution.

**Example (Success Case):**

```java
@DisplayName("positive: 숫자가 양수인 경우 값을 그대로 반환한다")
@Test
void positive_shouldReturnValue_whenValueIsPositive() {

    // Given
    Integer positiveValue = 5;

    // When
    Integer result = Guard.positive(positiveValue);

    // Then
    assertEquals(positiveValue, result);

}

```

#### Exception Case: Combining `When & Then`

For tests that expect an exception using `assertThrows`, the execution and verification are combined in a single method call. Therefore, it is standard practice to combine them into a `// When & Then` block.

**Example (Exception Case with Boundary Value):**

```java
@DisplayName("positive: 숫자가 양수가 아닌 경계값(0)에 대해 예외를 발생시킨다")
@Test
void positive_shouldThrowException_whenValueIsZero() {
    // Given
    Integer zero = 0;
    String errorMessage = "The number must be positive.";

    // When & Then
    var exception = assertThrows(
        IllegalArgumentException.class,
        () -> Guard.positive(zero, errorMessage)
    );
    assertEquals(errorMessage, exception.getMessage());

}
```



### Boundary Value Testing
Tests should not only cover typical cases but also focus on boundary values to ensure robustness. This includes, but is not limited to:

-   **Numeric values**: `0`, `-1`, `1`, `Integer.MIN_VALUE`, `Integer.MAX_VALUE`.
-   **Strings**: Empty string (`""`), blank string (`" "`), `null`.
-   **Collections**: Empty collections, collections with one element, `null` collections.
