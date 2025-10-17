# PROJECT_TECH.md

이 문서는 시스템의 기술적인 구현 세부사항을 다이어그램으로 명세합니다.
주요 내용은 **ERD(Entity-Relationship Diagram)**와 **주요 흐름에 대한 시퀀스 다이어그램**입니다.

---

## 1. ERD (Entity-Relationship Diagram)

H2 데이터베이스 스키마 기준의 ERD입니다.

```mermaid
erDiagram
    MEMBERS {
        varchar(36) member_id PK "UUID"
        varchar(50) username UK "로그인 ID"
        varchar(50) nickname UK "채팅 닉네임"
        varchar(255) hashed_password "해시된 비밀번호"
    }

    GUESTS {
        varchar(36) guest_id PK "UUID"
        varchar(50) nickname "채팅 닉네임"
    }

    REFRESH_TOKENS {
        varchar(255) token_value PK "토큰 값"
        varchar(36) member_id FK "Member ID"
        timestamp expiry_date "만료 일시"
    }

    MEMBERS ||--o{ REFRESH_TOKENS : "has"
```

**설계 요점:**
*   `MEMBERS`와 `GUESTS` 테이블은 완전히 분리되어 사용자의 유형을 명확히 구분합니다.
*   `username`과 `nickname`은 `MEMBERS` 테이블 내에서 고유해야 합니다 (UK: Unique Key).
*   `REFRESH_TOKENS` 테이블은 Member의 Refresh Token을 저장하여 상태 기반의 토큰 재발급을 지원합니다.

---

## 2. 시퀀스 다이어그램 (Sequence Diagrams)

### 2.1 회원 가입 (Member Registration)

```mermaid
sequenceDiagram
    actor Client
    participant C as AuthenticationController
    participant S as MemberRegistrationService
    participant P as CredentialPolicy
    participant H as PasswordHasher
    participant R as MemberRepository
    participant DB as Database

    Client->>+C: POST /api/auth/register/member (username, nickname, password)
    C->>+S: register(command)
    S->>+R: existsByUsernameOrNickname(...)
    R->>+DB: SELECT COUNT(*) FROM members WHERE ...
    DB-->>-R: count = 0
    R-->>-S: false
    S->>+P: check(username, nickname, password)
    P-->>-S: (Success, void)
    S->>+H: hash(password)
    H-->>-S: hashedPassword
    S->>S: Member.register(...)
    S->>+R: save(member)
    R->>+DB: INSERT INTO members (...)
    DB-->>-R: (Success)
    R-->>-S: savedMember
    S-->>-C: memberId
    C-->>-Client: 200 OK (Success)
```

### 2.2 회원 로그인 (Member Login)

```mermaid
sequenceDiagram
    actor Client
    participant C as AuthenticationController
    participant S as AuthenticationService
    participant Spring as Spring Security (AuthManager)
    participant D as MemberDetailsService
    participant R as MemberRepository
    participant J as JwtProvider
    participant DB as Database

    Client->>+C: POST /api/auth/login/member (username, password)
    C->>+S: loginMember(command)
    S->>+Spring: authenticate(username, password)
    Spring->>+D: loadUserByUsername(username)
    D->>+R: findByUsername(username)
    R->>+DB: SELECT * FROM members WHERE ...
    DB-->>-R: memberJpaEntity
    R-->>D: member
    D-->>-Spring: UserDetails (with hashed pass)
    Spring->>Spring: matches(rawPass, hashedPass)
    Spring-->>-S: Authentication (Success)
    S->>+J: generateAccessToken(member)
    J-->>-S: accessToken
    S->>+J: generateRefreshToken(member)
    J-->>-S: refreshToken
    S->>+R: save(refreshToken)
    R->>+DB: INSERT INTO refresh_tokens (...)
    DB-->>-R: (Success)
    R-->>-S:
    S-->>-C: TokenResponse
    C-->>-Client: 200 OK (TokenResponse)
```

### 2.3 게스트 로그인 (Guest Login)

```mermaid
sequenceDiagram
    actor Client
    participant C as AuthenticationController
    participant S as AuthenticationService
    participant GS as GuestLoginService
    participant G as GuestIdGenerator
    participant R as GuestRepository
    participant J as JwtProvider
    participant DB as Database

    Client->>+C: POST /api/auth/login/guest (nickname)
    C->>+S: loginGuest(command)
    S->>+GS: login(command)
    GS->>+G: generate()
    G-->>-GS: guestId
    GS->>GS: Guest.of(guestId, nickname)
    GS->>+R: save(guest)
    R->>+DB: INSERT INTO guests (...)
    DB-->>-R: (Success)
    R-->>-GS: savedGuest
    GS-->>-S: guest
    S->>+J: generateAccessToken(guest)
    J-->>-S: accessToken
    S-->>-C: TokenResponse (Access Token only)
    C-->>-Client: 200 OK (TokenResponse)
```
