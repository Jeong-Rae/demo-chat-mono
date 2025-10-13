# ENGINEERING_RULE

## 1) Common Rules
- VO는 `record` 사용.
- Fluent style(`.field()`), `getXxx()` 금지.
- Domain은 Spring 독립, pure Java (Lombok만 허용).
- Domain 구조: `.model`, `.service`.
- **Kent Beck TDD Cycle** 유지:
  - ① 테스트 작성 → ② 도메인/서비스 코드 구현 → ③ 개별 테스트 실행·검증.
  - 각 기능은 테스트 통과 후 리팩토링 단계를 거쳐 안정화.

---

## 2) Layer Responsibility

### domain
- 핵심: Entity, VO(record), Aggregate, DomainService, Policy/Spec.
- Port 정의: `OrderRepository`, `PaymentGateway`, `Clock`, `UuidGenerator`.
- 외부 기술 의존 금지. JPA 매핑 분리 권장.

### application
- UseCase orchestration: Command/Query, Tx boundary, Validation, Flow control.
- Inbound Port 제공, Outbound Port(domain 정의) 호출.
- 예외/반환: 단순·명료 (DomainException 매핑 등).

### infrastructure
- Outbound Adapter(DB/API/Cache 등) 구현.
- JPA ↔ Domain 매핑 계층 유지.
- Resilience(Timeout/Retry/CircuitBreaker/Bulkhead) 책임.

### interface
- Inbound Adapter: REST/GraphQL/CLI/Batch.
- Validation, Serialization, Error mapping, i18n 담당.
- Application 호출, Domain 규칙 포함 금지.

---

## 3) Value Object (VO)
- `record`, immutable, self-validation.
- Canonical constructor에서 검증.
- 행위 포함 가능 (변환/합성 등).

```java
public record Money(BigDecimal amount, String currency) {
    public Money { if (amount.signum() < 0) throw new DomainViolation(); }
}
````

---

## 4) Interface Composition

* 상속 대신 Capability interface 조합.
* 명시적 역할 분리, Test 용이성 확보.

```java
interface Identifiable<I> { I id(); }
interface Versioned { long version(); }
```

---

## 5) State Pattern

* 상태: Enum 사용.
* 매직 값: `static final`.
* 전이: Enum method 또는 State 객체 내부에 한정.

---

## 6) Documentation

* Public API에 JavaDoc 필수(interface/controller 제외).
* 단문체, `@example <pre>...</pre>` 사용.
* Return 표기: `var v = Model.action() // -> 42`

---