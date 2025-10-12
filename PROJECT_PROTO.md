좋습니다, Lyght님.
요청하신 대로 **프로토타입(실험용) 버전**에 초점을 맞춰,
**① Client 요청사항**, **② Use Case 명세**, **③ 도메인 언어 체계(용어집)**
이 세 축으로 명확하게 정의된 `PROJECT_PROTO.md`를 아래에 제시합니다.

ERD/UML은 후속 문서에서 다루기 때문에 이 버전은 **도메인 언어와 행위 정의 중심**으로 구성했습니다.

---
# PROJECT_PROTO.md

## 1. 프로젝트 개요

본 프로젝트는 실험 단계의 **1:1 채팅 프로토타입 시스템**으로,  
핵심 도메인 모델과 유즈케이스 구조를 빠르게 검증하기 위한 목적을 가진다.

아키텍처는 **DDD + Hexagonal Architecture (Ports & Adapters)** 원칙을 따르며,  
모든 구성 요소는 단일 모듈 안에서 계층적으로 구분된다.

---

## 2. 개발 환경 및 제약 조건

| 항목 | 내용 |
|------|------|
| **모듈 구조** | 단일 모듈 (monolith) |
| **아키텍처 패턴** | DDD + Hexagonal Architecture |
| **통신 프로토콜** | WebSocket (STOMP 프레임 기반) |
| **인증/인가** | 비활성화, username/password 기반 Guest 연결만 허용 |
| **채팅 형태** | 1:1 채팅 (Group Chat 미포함) |
| **Persistence** | 메모리(In-Memory) 저장소 사용 |
| **Message Broker** | 미사용 (단일 인스턴스 기준) |
| **목표** | 도메인 구조, 용어 체계, 유즈케이스 명확화 |

---

## 3. Client 요청 사항

### 3.1 기능 요구
- 사용자는 username/password를 통해 Guest 세션으로 접속한다.
- 접속 후 특정 상대방과 1:1 채팅방을 자동으로 생성하거나 기존 방에 입장한다.
- 채팅 메시지는 WebSocket을 통해 송수신된다.
- 서버는 송신자와 수신자 세션만 대상으로 메시지를 전달한다.
- 연결 종료 시 세션 및 방 정보는 즉시 메모리에서 제거된다.
- 서버 재기동 시 모든 데이터는 초기화된다.
- 클라이언트는 단일 서버 인스턴스에만 연결한다.

### 3.2 비기능 요구
- 최소한의 리소스로 구동 가능해야 한다. (Local run, no infra dependency)
- 메시지 전송 지연이 100ms 이하일 것.
- 외부 인증 시스템 연동 없음.
- 재연결 시 세션 복원 없이 신규 세션으로 간주.

---

## 4. Use Case 명세

### 4.1 UC-01 : 사용자 접속 (Join)
**목적**  
사용자가 WS(STOMP)를 통해 서버에 접속하여 Guest 세션을 생성한다.

**흐름**
1. 클라이언트가 STOMP CONNECT 프레임 전송 (username, password 포함)
2. 서버는 username/password를 검증 (단순 문자열 매칭 수준)
3. `ChatUser` 객체를 생성하고 세션과 바인딩
4. 시스템에 `JOIN` 이벤트 발행 (`/topic/system` 구독자에 알림)
5. 응답 프레임: “JOIN_SUCCESS” 메시지

**예외 흐름**
- username 중복 시 “ALREADY_CONNECTED” 오류 반환
- password 미일치 시 “AUTH_FAILED” 반환

---

### 4.2 UC-02 : 1:1 채팅방 생성 (CreateRoom)
**목적**  
두 사용자의 대화 컨텍스트를 생성하거나 기존 Room을 반환한다.

**흐름**
1. 사용자가 채팅 요청 시, 서버는 `ChatRoomRegistry`에서 Room 존재 여부 확인
2. 존재하지 않으면 새로운 `ChatRoom` 생성 (roomId = chat:{u1}:{u2})
3. 양쪽 세션에 “ROOM_CREATED” 또는 “ROOM_JOINED” 이벤트 발송

**비즈니스 규칙**
- Room은 항상 2명의 유효 사용자로 구성되어야 한다.
- 동일 사용자 간 중복 Room 생성 불가.

---

### 4.3 UC-03 : 메시지 전송 (SendMessage)
**목적**  
사용자가 채팅방 내에서 메시지를 전송한다.

**흐름**
1. 클라이언트 → `/app/chat.send` 프레임 전송
   ```json
   { "roomId": "chat:u1:u2", "sender": "u1", "content": "안녕!" }
````

2. 서버는 `ChatCommandService`를 통해 `ChatMessage` 생성
3. 도메인 검증 (빈 메시지, 미가입자 전송 등)
4. `ChatMessage`를 수신자 세션으로 전달
5. 송신자에게는 Echo 메시지 반환 (확인용)

**예외 흐름**

* 유효하지 않은 roomId → “ROOM_NOT_FOUND”
* 송신자가 Room 참가자가 아닐 경우 → “FORBIDDEN”

---

### 4.4 UC-04 : 연결 종료 (Leave)

**목적**
사용자가 연결을 종료하거나 세션이 종료될 때 관련 리소스를 정리한다.

**흐름**

1. STOMP DISCONNECT 이벤트 수신
2. `SessionRegistry`에서 해당 세션 삭제
3. 참여 중이던 ChatRoom에서 사용자 제거
4. 남은 상대방에게 “LEAVE” 알림 전송
5. 로그에 세션 종료 기록 남김

---

## 5. 도메인 언어 체계 (Ubiquitous Language)

| 용어                     | 정의                                | 추가 설명                                                          |
| ---------------------- | --------------------------------- | -------------------------------------------------------------- |
| **ChatUser**           | 시스템에 접속한 사용자를 나타내는 객체             | Guest 사용자이며 username으로 식별됨                                     |
| **ChatRoom**           | 두 명의 사용자가 대화하는 1:1 컨텍스트           | `chat:{userA}:{userB}` 형태의 RoomId로 식별                          |
| **ChatMessage**        | 사용자 간 주고받는 실제 대화 단위               | `sender`, `content`, `sentAt`을 포함                              |
| **ChatSession**        | WebSocket 세션과 사용자 간의 연결 상태        | username, sessionId, connected 상태 포함                           |
| **ChatCommandService** | 유즈케이스 실행을 담당하는 애플리케이션 서비스         | Join, Message, Leave 등                                         |
| **ChatRoomRegistry**   | 현재 활성화된 Room 목록을 관리               | RoomId 기준으로 Map 형태 저장                                          |
| **SessionRegistry**    | 현재 연결된 세션과 사용자 매핑 관리              | sessionId → ChatUser                                           |
| **Join**               | 사용자가 WS에 연결되어 세션을 생성하는 행위         | CONNECT 프레임 수신 시 발생                                            |
| **Message**            | 채팅 메시지 전송 행위                      | `/app/chat.send` 경로를 통해 수행                                     |
| **Leave**              | 사용자가 연결을 종료하는 행위                  | DISCONNECT 프레임 수신 시 발생                                         |
| **Guest**              | 인증되지 않은 임시 사용자                    | username/password로만 식별                                         |
| **RoomId**             | 두 사용자의 username으로 구성된 방 식별자       | `chat:{min(usernameA, usernameB)}:{max(usernameA, usernameB)}` |
| **MessageId**          | 개별 메시지의 유니크 ID                    | UUID 기반 생성                                                     |
| **ChatText**           | 채팅 메시지 본문 VO                      | 빈 문자열 또는 null 금지                                               |
| **ChatEvent**          | 시스템 내 발생하는 Join/Leave/Message 이벤트 | WS 브로드캐스트에 활용 가능                                               |

---

## 6. 시스템 행위 요약

| 행위              | 주체              | 설명                               |
| --------------- | --------------- | -------------------------------- |
| **Connect**     | Client          | username/password로 STOMP CONNECT |
| **Join 처리**     | Server          | 세션 생성 후 시스템에 JOIN 이벤트 등록         |
| **Room 생성/조회**  | Server          | RoomId 계산 후 존재 여부 확인 및 생성        |
| **Message 송신**  | Client → Server | `/app/chat.send` 전송              |
| **Message 팬아웃** | Server          | 송신자/수신자 세션에게 동일 메시지 전송           |
| **Disconnect**  | Client / Server | 연결 종료 시 세션 제거 및 LEAVE 이벤트 처리     |

---

## 7. 향후 확장 포인트

* **권한 모델 추가**: 인증/인가 서비스 연동, Role 도입
* **Group Chat 확장**: RoomMembership Entity 추가
* **메시지 영속화**: Redis Streams 또는 DB 저장
* **도메인 이벤트 발행**: ChatWireMessage 기반 이벤트 버스 구조 적용
* **다중 노드 확장**: Redis Pub/Sub 기반 메시지 공유

---

*본 문서는 실험 단계 프로토타입의 행위 정의 및 도메인 언어 확립을 목적으로 하며,
ERD, 클래스 다이어그램, 시퀀스 다이어그램은 별도 설계 문서에서 정의한다.*

```

---

이 버전은 실제 개발 프로토타입에서:
- 개발자가 **동일한 언어로 대화할 수 있는 도메인 공통어**를 확보하고,  
- **핵심 유즈케이스의 경계를 명확히 정의**하며,  
- **미래 확장(권한, 메시지버스, ACL)** 으로 쉽게 발전할 수 있는 기반이 됩니다.
```
