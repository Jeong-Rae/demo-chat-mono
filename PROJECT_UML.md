# PROJECT_UML.md

아래는 **VO, AGGREGATE, ENTITY가 명확히 구분**된 상태에서 **상호 관계를 도식화한 UML 클래스 다이어그램**입니다.
(프로토타입: 1:1 채팅, DDD + Hex, 단일 모듈, STOMP/WS, 인증/인가 비활성)

> 표기: `<<AggregateRoot>>`, `<<Entity>>`, `<<ValueObject>>`, `<<Repository>>`

```mermaid
classDiagram
direction TB

%% ========= Aggregate Roots =========
class ChatRoom {
  <<AggregateRoot>>
  - roomId: RoomId
  - participants: Set<UserId>  // size=2 (invariant)
  - createdAt: Instant
  + canSend(sender: UserId): boolean
  + addMessage(msg: ChatMessage): void
  + hasParticipant(user: UserId): boolean
}

class ChatUser {
  <<AggregateRoot>>
  - userId: UserId
  - username: String
  - joinedAt: Instant
  + is(user: UserId): boolean
}

%% =============== Entities ===============
class ChatMessage {
  <<Entity>>
  - messageId: MessageId
  - roomId: RoomId
  - senderId: UserId
  - text: ChatText
  - sentAt: Instant
}

%% =============== Value Objects ===============
class RoomId {
  <<ValueObject>>
  - value: String   // "chat:{min(u1,u2)}:{max(u1,u2)}"
  + equals()
  + hashCode()
}

class UserId {
  <<ValueObject>>
  - value: String   // username 기반
  + equals()
  + hashCode()
}

class MessageId {
  <<ValueObject>>
  - value: String   // UUID
  + equals()
  + hashCode()
}

class ChatText {
  <<ValueObject>>
  - value: String   // not blank, length <= 4000
  + equals()
  + hashCode()
}

%% =============== Repositories (Ports) ===============
class ChatRoomRepository {
  <<Repository>>
  + findById(id: RoomId): Optional~ChatRoom~
  + save(room: ChatRoom): void
}

class ChatUserRepository {
  <<Repository>>
  + findById(id: UserId): Optional~ChatUser~
  + save(user: ChatUser): void
}

%% =============== Relationships ===============
ChatRoom "1" *-- "0..*" ChatMessage : contains
ChatRoom "1" --> "1" RoomId : identifies
ChatMessage "1" --> "1" MessageId : identifies
ChatMessage "1" --> "1" RoomId : belongsTo
ChatRoom "1" --> "2" UserId : participants
ChatMessage "1" --> "1" UserId : sender
ChatMessage "1" --> "1" ChatText : content

ChatUser "1" --> "1" UserId : identifies

ChatRoomRepository ..> ChatRoom
ChatUserRepository ..> ChatUser
```

## 설계 요점(간략)

* **Aggregate Root**

  * `ChatRoom`: 1:1 대화의 **컨텍스트**와 **불변식(참여자 2명)**을 보장. 메시지 수집/행위의 트랜잭션 경계.
  * `ChatUser`: 게스트 사용자의 **정체성**을 보유(프로토타입에선 최소 모델).
* **Entity**

  * `ChatMessage`: 식별자(`MessageId`)를 가진 **변별 가능한 메시지** 단위. `ChatRoom`의 **구성 요소(Composition)**.
* **Value Object**

  * `RoomId`, `UserId`, `MessageId`, `ChatText`는 **동등성/불변/검증 규칙**을 캡슐화.
* **Repository(Port)**

  * 도메인 주도의 저장/조회 계약. 실제 저장소는 Adapter로 치환.

> **의사결정 포인트**(프로토타입 가정)
>
> * 메시지는 `ChatRoom` 내부 **구성요소**(Composition)로 모델링하여 **도메인 규칙(참여자 검증, canSend)**을 루트에서 일관 적용.
> * `ChatUser`는 다른 애그리게이트로 독립시키되, `ChatRoom`은 **UserId(VO)만 참조**하여 결합도를 낮춤.
> * 세션/WS 연결 등은 **도메인 외부(Interfaces/Infrastructure)**에서 다룹니다.