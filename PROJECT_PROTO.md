# PROJECT_PROTO.md

## 1. 프로젝트 개요

본 프로젝트는 **1:1 채팅 시스템**으로, JWT 기반 인증을 도입하고 Member와 Guest 사용자를 명확히 분리하여 도메인 모델의 완성도를 높이는 것을 목표로 한다.

아키텍처는 **DDD + Hexagonal Architecture (Ports & Adapters)** 원칙을 따르며, 모든 구성 요소는 단일 모듈 안에서 계층적으로 구분된다.

---

## 2. 개발 환경 및 제약 조건

| 항목 | 내용 |
|------|------|
| **모듈 구조** | 단일 모듈 (monolith) |
| **아키텍처 패턴** | DDD + Hexagonal Architecture |
| **통신 프로토콜** | WebSocket (STOMP), HTTP/S (for Auth) |
| **인증/인가** | JWT 기반 stateless 인증. Member와 Guest 흐름 분리. |
| **채팅 형태** | 1:1 채팅 (Group Chat 미포함) |
| **Persistence** | Spring Data JPA / H2 In-Memory DB |
| **Message Broker** | 미사용 (단일 인스턴스 기준) |
| **목표** | 도메인 구조, 용어 체계, 유즈케이스 명확화 |

---

## 3. Client 요청 사항

### 3.1 기능 요구
- **Member**는 `username`, `nickname`, `password`를 사용해 **회원가입**을 할 수 있다.
- **Member**는 `username`, `password`를 사용해 **로그인**하고, **Access/Refresh Token**을 발급받는다.
- **Guest**는 `nickname`만으로 **로그인**하여 **Access Token**을 발급받는다.
- 모든 채팅 API 요청은 유효한 Access Token을 필요로 한다.
- 접속 후 특정 상대방과 1:1 채팅방을 자동으로 생성하거나 기존 방에 입장한다.
- 채팅 메시지는 WebSocket을 통해 송수신된다.
- 서버 재기동 시 H2 데이터베이스는 초기화된다.

### 3.2 비기능 요구
- 인증 로직은 stateless 하게 유지되어야 한다.
- Member의 비밀번호는 Argon2 알고리즘으로 안전하게 해시되어 저장된다.
- 외부 인증 시스템 연동 없음.

---

## 4. Use Case 명세

### 4.1 UC-01 : 회원 가입 (Member Registration)
**목적**  
사용자가 시스템에 영구적인 Member 계정을 생성한다.

**흐름**
1. 클라이언트가 `POST /api/auth/register/member`로 `username`, `nickname`, `password` 전송
2. `MemberRegistrationService`가 `CredentialPolicy`를 통해 자격 증명 규칙을 검증한다.
3. `MemberRepository`를 통해 `username`과 `nickname`의 중복 여부를 확인한다.
4. `PasswordHasher`를 통해 비밀번호를 해싱하고, 새로운 `Member`를 생성하여 저장한다.
5. 응답: "회원가입 성공" 메시지

**예외 흐름**
- 규칙 위반 시 `400 Bad Request`와 오류 코드 반환
- `username` 또는 `nickname` 중복 시 `409 Conflict` 반환

---

### 4.2 UC-02 : 회원 로그인 (Member Login)
**목적**  
기존 Member가 자신의 신원을 증명하고 서비스 접근 토큰을 발급받는다.

**흐름**
1. 클라이언트가 `POST /api/auth/login/member`로 `username`, `password` 전송
2. `AuthenticationService`가 Spring Security를 통해 인증을 수행한다.
3. 인증 성공 시, **Access Token**과 **Refresh Token**을 생성하여 반환한다.
4. Refresh Token은 DB에 저장하여 추후 재발급에 사용한다.

**예외 흐름**
- 자격 증명 불일치 시 `401 Unauthorized` 반환

---

### 4.3 UC-03 : 게스트 로그인 (Guest Login)
**목적**  
임시 사용자가 닉네임만으로 시스템에 입장하여 임시 토큰을 발급받는다.

**흐름**
1. 클라이언트가 `POST /api/auth/login/guest`로 `nickname` 전송
2. `GuestLoginService`가 새로운 `Guest` 애그리게이트를 생성하고 저장한다.
3. **Access Token**을 생성하여 반환한다. (Refresh Token 없음)

---

### 4.4 UC-04 : 1:1 채팅방 생성 (CreateRoom)
**목적**  
두 사용자의 대화 컨텍스트를 생성하거나 기존 Room을 반환한다.

**흐름**
1. 사용자가 채팅 요청 시, 서버는 `ChatRoomRepository`에서 Room 존재 여부 확인
2. 존재하지 않으면 새로운 `ChatRoom` 생성 (roomId = chat:{u1.id}:{u2.id})
3. 양쪽 세션에 “ROOM_CREATED” 또는 “ROOM_JOINED” 이벤트 발송

**비즈니스 규칙**
- Room은 항상 2명의 유효 사용자로 구성되어야 한다.
- 동일 사용자 간 중복 Room 생성 불가.

---

### 4.5 UC-05 : 메시지 전송 (SendMessage)
**목적**  
사용자가 채팅방 내에서 메시지를 전송한다.

**흐름**
1. 클라이언트 → `/app/chat.send` STOMP 프레임 전송
2. 서버는 `ChatCommandService`를 통해 `ChatMessage` 생성 및 도메인 검증
3. `ChatMessage`를 수신자 세션으로 전달

**예외 흐름**
- 유효하지 않은 roomId → “ROOM_NOT_FOUND”
- 송신자가 Room 참가자가 아닐 경우 → “FORBIDDEN”

---

## 5. 도메인 언어 체계 (Ubiquitous Language)

| 용어 | 정의 | 추가 설명 |
|---|---|---|
| **Member** | 회원가입을 통해 생성된 영구 사용자 애그리게이트. | `username`, `nickname`, `hashedPassword`를 가짐. |
| **Guest** | 닉네임으로 식별되는 임시 사용자 애그리게이트. | 임시 Access Token만 발급받음. |
| **CredentialPolicy** | 자격 증명의 형식과 규칙을 검증하는 도메인 정책. | 비밀번호 강도, 사용자 이름 형식 등을 정의. |
| **Password** | 비밀번호 원문을 표현하는 값 객체. | `PasswordStrength`를 계산하는 책임을 가짐. |
| **ChatRoom** | 두 명의 사용자가 대화하는 1:1 컨텍스트. | `chat:{userA.id}:{userB.id}` 형태의 RoomId로 식별. |
| **ChatMessage** | 사용자 간 주고받는 실제 대화 단위. | `sender`, `content`, `sentAt`을 포함. |
| **AuthenticationService**| 로그인 유즈케이스를 담당하는 애플리케이션 서비스. | Member/Guest 로그인 흐름을 오케스트레이션. |
| **MemberRegistrationService**| 회원가입 유즈케이스를 담당하는 애플리케이션 서비스. | |
| **RoomId** | 두 사용자의 ID로 구성된 방 식별자. | `chat:{min(idA, idB)}:{max(idA, idB)}` |
| **MemberId / GuestId** | 각 애그리게이트를 식별하는 고유 값 객체. | UUID 기반으로 생성. |
| **ChatText** | 채팅 메시지 본문 VO. | 빈 문자열 또는 null 금지. |

---
*본 문서는 JWT 인증 기반 시스템의 행위 정의 및 도메인 언어 확립을 목적으로 하며, ERD, 클래스 다이어그램, 시퀀스 다이어그램은 별도 설계 문서에서 정의한다.*