# DDD Convention

## 1) 공통 개발 규칙

- 모든 VO는 Record를 사용하여 구성된다.
- 모든 참조 메서드는 Fluent 스타일의 네이밍을 사용하여 도메인 의미를 살린다.(java bean 스타일의 getXXX() 금지)
- Domain 레이어는 Spring 의존성이 없는 순수 java 객체여야한다. (Lombok만 외부 라이브러리 허용)
- domain 레이어는 .model과 .service로 분리된다.

## 2) 레이어별 책임 (domain / application / infrastructure / interface)

### domain

* **핵심 모델**: Entity, Value Object(record), Aggregate, Domain Service, Policy/Specification.
* **Port(Interface) 정의**: `OrderRepository`, `PaymentGateway`, `Clock`, `UuidGenerator` 등.
* 외부 기술 의존 금지(표준 애너테이션 제외). JPA 매핑은 분리 권장.

### application

* **Use Case 오케스트레이션**: Command/Query 처리, 트랜잭션 경계, 권한·검증·서브시스템 호출 순서 제어.
* **Inbound Port**(Use Case 인터페이스) 제공, **Outbound Port**(domain이 정의한 Port) 호출.
* 반환/에러 전파는 **단순 명료**하게 처리한다(도메인 예외 매핑 등 팀 표준 준수).

### infrastructure

* **Outbound Adapter** 구현: DB(JPA), 외부 API, 캐시, 파일 등.
* 영속 모델(JPA Entity) ↔ 도메인 모델 **매핑 계층** 유지.
* 회복력(Timeout/Retry/Circuit Breaker/Bulkhead) 등 **기술 품질** 책임.

### interface

* **Inbound Adapter**: REST Controller, GraphQL, CLI, Batch 등.
* 입력 검증/직렬화/에러 매핑(HTTP 코드)/국제화 등 **표현 계층** 책임.
* Application의 Use Case를 호출하며 **도메인 규칙 미포함**.

---

## 4) Value Object(VO) 규칙 — `record` 사용

* **불변/동등성 기반 비교**(value equality), **자기 검증** 포함.
* **행위 보유 가능**: 단위 변환, 합성, 검증 로직 등.
* 유효성 검사는 **canonical constructor** 에서 수행.

```java
public record Money(BigDecimal amount, String currency) {
    public Money {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);
        if (amount.scale() > 2 || amount.signum() < 0) {
            throw new DomainViolation("Invalid amount");
        }
    }
    public Money add(Money other) { /* 통화 일치 검증 후 합산 */  }
}

public record OrderId(UUID value) {
    public OrderId { Objects.requireNonNull(value); }
    @Override public String toString() { return value.toString(); }
}
```

---

## 5) **Interface 조합(Composition) 규칙** — 특성(속성) 부여

* 객체에 능력·특성(capability)을 **인터페이스 조합**으로 부여한다(상속 남용 금지).
* 예) 추적/감사/버전/소유자 등 공통 속성은 **Capability 인터페이스**로 나누고, 구현체는 필요한 조합만 채택한다.
* 컴파일 타임 계약을 통한 **명시적 역할 분리**와 **테스트 용이성** 확보.

```java
public interface Identifiable<I> { I id(); }
public interface Versioned { long version(); }

public final class Document implements Identifiable<DocumentId>, Versioned {
    private final DocumentId id;
    private final long version;
    private final UserId ownerId;
    public DocumentId id() { return id; }
    public long version() { return version; }
}
```

---

---

## 6) 상태(State) 패턴 규칙

* 상태 상수는 **Enum** 으로 표현하고, 내부 상수/매직 값은 **`static final`** 로 관리한다.
* 상태 전이는 **상태 객체** 또는 **Enum 메서드**에서 한정적으로 허용한다(불변식 검증 내장).

## 7) 문서화 규칙
- Public으로 노출되는 API는 반드시 JAVA Doc을 작성한다. (interface나 controller제외)
- java doc에서는 `단답형 문체를 사용한다.`
- 복잡한 api는 `@example <pre>code block</pre>`을 통해 사용법을 안내한다. reurn식을 표현할떄는 `var value = Model.action() // -> 42`와 같은 형식으로 반환 값을 표현한다.
