# Project Conventions Guide

This document provides a high-level overview of the key development conventions used in this project. For detailed information, please refer to the linked guide for each section.

---

## 1. Domain-Driven Design (DDD) Convention

This project follows specific Domain-Driven Design (DDD) principles to ensure a clean, maintainable, and scalable architecture. The rules cover layer responsibilities, Value Object (VO) implementation, and documentation standards.

- **Key Principles**:
    - The Domain layer must remain pure and independent of frameworks like Spring.
    - Value Objects are implemented using immutable Java `record`s.
    - Clear separation of responsibilities between `domain`, `application`, `infrastructure`, and `interface` layers.

- **Detailed Guide**: [DDD_CONVENTION.md](../DDD_CONVENTION.md)

---

## 2. Guard Utility Usage

A framework-agnostic `Guard` utility is used for precondition and invariant checking throughout the domain layer. It helps ensure data validity and object state consistency with concise, readable code.

- **Key Features**:
    - Fluent interface and method chaining.
    - Lazy evaluation of error messages using `Supplier<String>` for better performance.
    - Clear separation of exception types (`NullPointerException`, `IllegalArgumentException`, `IllegalStateException`).

- **Detailed Guide**: [Guard_Usage_Guide.md](../Guard_Usage_Guide.md)

---

## 3. Error Handling Strategy

The project employs a unified, domain-centric error handling strategy. All business rule violations within the domain layer are signaled exclusively through a single `DomainException`.

- **Core Components**:
    - `DomainErrorCode`: An interface for domain-specific error enums.
    - `DomainException`: The single exception type thrown by the domain.
    - `ErrorDetails`: An immutable VO for carrying structured error context.
    - `<Domain>ErrorCode`: Enums that implement `DomainErrorCode` and act as factories for creating exceptions.

- **Detailed Guide**: [Error_Handle_Guide.md](../Error_Handle_Guide.md)

---

## 4. Test Convention

This project adheres to a strict Test-Driven Development (TDD) cycle. All development of domain models, services, and utilities must begin with a failing test, followed by the implementation to make it pass, and finally refactoring.

- **Key Principles**:
    - Follows Kent Beck's "Red-Green-Refactor" TDD cycle.
    - Tests are structured using the `Given-When-Then` pattern.
    - Test method names must be descriptive (`feature_shouldExpectedBehavior_whenCondition`).
    - `@DisplayName` annotations in Korean are mandatory for all tests.

- **Detailed Guide**: [TEST_CONVENTION.md](../TEST_CONVENTION.md)