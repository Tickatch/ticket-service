# Tickatch Ticket Service

티켓 예매 플랫폼 Tickatch의 티켓 관리 마이크로서비스입니다.

## 프로젝트 소개

Ticket Service는 티켓과 관련된 전 과정의 상태 흐름을 관리하며, 예매 상태와의 동기화를 통해 안정적인 티켓 발행 처리를 담당합니다.

## 기술 스택

| 분류            | 기술                       |
|---------------|--------------------------|
| Framework     | Spring Boot 3.x          |
| Language      | Java 21                  |
| Database      | PostgreSQL               |
| Messaging     | RabbitMQ                 |
| Query         | QueryDSL / JPA           |
| Communication | OpenFeign / RestTemplate |
| Security      | Spring Security          |

## 아키텍처


### 시스템 구성

```
┌────────────────────────────────────────────────────────────┐
│                          Tickatch Platform                 │
├──────────────┬──────────────┬──────────────┬───────────────┤
│     User     │     Auth     │   Product    │    ArtHall    │
│   Service    │   Service    │   Service    │    Service    │
├──────────────┼──────────────┼──────────────┼───────────────┤
│ Reservation  │    Ticket    │ Reservation  │    Payment    │
│   Service    │   Service    │     Seat     │    Service    │
│              │              │   Service    │               │
├──────────────┴──────────────┴──────────────┴───────────────┤
│    Notification Service     │         Log Service          │
└────────┬────────────────────┴──────────┬───────────────────┘
         │                               │
         └────────────────────┬──────────┘
                              │
                          RabbitMQ
```

### 레이어 구조

```
src/main/java/com/tickatch/ticketservice
├── TicketServiceApplication.java
├── global
│   ├── config
│   │   ├── AuthExtractor.java
│   │   ├── FeignConfig.java
│   │   ├── FeignErrorDecoder.java
│   │   ├── FeignRequestInterceptor.java
│   │   └── SecurityConfig.java
│   ├── domain
│   │   ├── AbstractAuditEntity.java
│   │   └── AbstractTimeEntity.java
│   └── security
│       └── ActorExtractor.java
└── ticket
    ├── application
    │   ├── dto
    │   │   ├── TicketActionResponse.java
    │   │   ├── TicketDetailResponse.java
    │   │   ├── TicketRequest.java
    │   │   └── TicketResponse.java
    │   ├── event
    │   │   ├── TicketIssuedEvent.java
    │   │   └── TicketIssuedEventListener.java
    │   ├── messaging
    │   │   └── event
    │   │       └── ProductCancelledEvent.java
    │   ├── port
    │   │   ├── TicketEventPublisherPort.java
    │   │   └── TicketLogPort.java
    │   └── service
    │       └── TicketService.java
    ├── domain
    │   ├── ReceiveMethod.java
    │   ├── SeatInfo.java
    │   ├── Ticket.java
    │   ├── TicketId.java
    │   ├── TicketStatus.java
    │   ├── dto
    │   │   ├── ProductInfo.java
    │   │   ├── ReservationInfo.java
    │   │   └── UserInfo.java
    │   ├── event
    │   │   └── TicketIssuedDomainEvent.java
    │   ├── exception
    │   │   ├── TicketErrorCode.java
    │   │   └── TicketException.java
    │   ├── repository
    │   │   ├── TicketDetailsRepository.java
    │   │   └── TicketRepository.java
    │   └── service
    │       ├── ProductService.java
    │       ├── ReservationService.java
    │       └── UserService.java
    ├── infrastructure
    │   ├── api
    │   │   ├── ProductServiceImpl.java
    │   │   ├── ReservationServiceImpl.java
    │   │   └── UserServiceImpl.java
    │   ├── client
    │   │   ├── ProductFeignClient.java
    │   │   ├── ReservationFeignClient.java
    │   │   ├── UserFeignClient.java
    │   │   └── dto
    │   │       ├── ProductClientResponse.java
    │   │       ├── ReservationClientResponse.java
    │   │       └── UserClientResponse.java
    │   ├── messaging
    │   │   ├── config
    │   │   │   └── RabbitMQConfig.java
    │   │   ├── consumer
    │   │   │   └── ProductCancelledEventConsumer.java
    │   │   ├── event
    │   │   │   └── TicketLogEvent.java
    │   │   └── publisher
    │   │       ├── TicketEventPublisher.java
    │   │       └── TicketLogPublisher.java
    │   └── persistence
    │       └── config
    │           └── JPAConfig.java
    └── presentation
        ├── dto
        │   └── CreateTicketRequest.java
        └── webapi
            └── TicketApi.java
```

## 도메인 모델

### Ticket (Aggregate Root)

티켓의 전체 라이프사이클을 관리하는 핵심 엔티티입니다.

```
Ticket
├── 기본 정보
│   ├── ticketId           # 티켓 ID
│   ├── reservationId      # 예매 ID
│   └── productId          # 상품 ID
│
├── 좌석 정보
│   └── SeatInfo
│
├── 수령 방법
│   └── ReceiveMethod 
│
└── 상태
    └── TicketStatus
```

### Value Objects

| VO            | 설명       | 주요 필드                           |
|---------------|----------|---------------------------------|
| TicketStatus  | 티켓 상태    | ISSUED, USED, CANCELED, EXPIRED |
| ReceiveMethod | 티켓 수령 방식 | ON_SITE, EMAIL, MMS             |

### SeatInfo

티켓 발급 대상 좌석과 관련된 정보를 관리하는 엔티티입니다. Ticket에 종속됩니다.

| 필드          | 설명    |
|-------------|-------|
| seatId   | 좌석 id |
| grade | 좌석 등급 |
| seatNumber  | 좌석번호  |
| price       | 가격    |

## 티켓 상태(TicketStatus)

### 상태 종류

| 상태 | 설명 | 최종 상태 |
|------|------|:--------:|
| `ISSUED` | 발행됨 - 티켓 발행 완료 | ❌ |
| `USED` | 사용됨 - 공연 입장 완료 | ✅ |
| `CANCELED` | 취소됨 - 예매 취소로 인한 티켓 취소 | ✅ |
| `EXPIRED` | 만료됨 - 공연 종료로 인한 만료 | ✅ |

### 상태 전이 다이어그램

```
                    ┌────────────┐
                    │  CANCELED  │ (최종)
                    └────────────┘
                         ↑
                         │ (예매 취소)
                         │
        ┌──────────┐     │     ┌──────────┐
        │  ISSUED  │─────┴────→│   USED   │ (최종)
        └──────────┘           └──────────┘
             │
             │ (공연 종료)
             ↓
        ┌──────────┐
        │ EXPIRED  │ (최종)
        └──────────┘
```

## 주요 기능

### 티켓 관리

- 티켓 생성(ISSUED 상태로 시작)
    - 예매가 CONFIRMED 상태인지 확인 후 티켓 생성
    - 이미 발행된 티켓 존재하면 취소 후 재발행
- 티켓 조회
    - 티켓 단건 조회/목록 조회
- 티켓 취소
  - 예매 취소 시 티켓 취소
  - 티켓만 취소
- 티켓 사용


### 티켓 상태 관리

- 상태 관리
    - 티켓 상태 전이 관리 : ISSUED → USED / CANCELED
    - 예매에 따른 상태 동기화

## API 명세

Base URL: `/api/v1/tickets`

### 조회

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|:----:|
| GET | `/{id}` | 티켓 상세 조회 | ✅ |
| GET | `/` | 티켓 목록 조회 | ✅ |

### 발행

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|:----:|
| POST | `/` | 티켓 발행 | ✅ |

### 상태 관리

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|:----:|
| POST | `/{id}/use` | 티켓 사용 처리 | ✅ |
| POST | `/{reservationId}/cancel` | 예매 취소로 인한 티켓 취소 | ✅ |

### Request DTOs

#### CreateTicketRequest (티켓 발행)

| 구분 | 필드 | 타입 | 필수 | 설명 |
|------|------|------|:----:|------|
| **예매 정보** | reservationId | UUID | ✅ | 예매 ID |
| **상품 정보** | productId | Long | | 상품 ID |
| **좌석 정보** | seatId | Long | | 좌석 ID |
| | seatNumber | String | ✅ | 좌석 번호 (예: A1, B12) |
| | grade | String | ✅ | 좌석 등급 (VIP, R, S 등) |
| **결제 정보** | price | Long | | 가격 (원) |
| **수령 정보** | receiveMethod | ReceiveMethod | | 티켓 수령 방법 |


## 이벤트

### 발행 이벤트 (Producer)

| 이벤트               | Routing Key                          | 대상 서비스               | 설명            |
|-------------------|--------------------------------------|----------------------|---------------|
| TicketIssuedEvent | `ticket.issued.notification` | Notification Service | 티켓 발행 시 알림 발송 |


## 외부 연동

### Feign Client

| 서비스               | 용도          |
|-------------------|-------------|
| ProductClient     | 상품 관련 정보 수집 |
| ReservationClient | 예매 관련 정보 수집 |
| UserClient        | 예매자 정보 수집   |

## 실행 방법

### 환경 변수

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tickatch
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  rabbitmq:
    host: localhost
    port: 5672
    username: ${RABBITMQ_USERNAME}
    password: ${RABBITMQ_PASSWORD}
```

### 실행

```bash
./gradlew bootRun
```

### 테스트

```bash
./gradlew test
```

### 코드 품질 검사

```bash
./gradlew spotlessApply spotbugsMain spotbugsTest
```

## 데이터 모델

### ERD

```
┌─────────────────────────────────────────────────────────────────┐
│                          p_ticket                               │
├─────────────────────────────────────────────────────────────────┤
│ id                    UUID PK                                   │
├─────────────────────────────────────────────────────────────────┤
│ -- 연관 정보 --                                                   │
│ reservation_id        UUID NOT NULL                             │
│ product_id            BIGINT NOT NULL                           │
│ seat_id               BIGINT NOT NULL                           │
├─────────────────────────────────────────────────────────────────┤
│ -- 좌석/등급 정보 --                                               │
│ seat_number           VARCHAR(20) NOT NULL                      │
│ grade                 VARCHAR(20) NOT NULL                      │
│ price                 BIGINT NOT NULL                           │
├─────────────────────────────────────────────────────────────────┤
│ -- 티켓 관리 --                                                   │
│ receive_method        VARCHAR(20) NOT NULL                      │
│ status                VARCHAR(20) NOT NULL                      │
├─────────────────────────────────────────────────────────────────┤
│ -- 이력 정보 --                                                   │
│ issued_at             TIMESTAMP                                 │
│ used_at               TIMESTAMP                                 │
│ canceled_at           TIMESTAMP                                 │
├─────────────────────────────────────────────────────────────────┤
│ -- Audit --                                                     │
│ created_at            TIMESTAMP NOT NULL                        │
│ created_by            VARCHAR(100) NOT NULL                     │
│ updated_at            TIMESTAMP NOT NULL                        │
│ updated_by            VARCHAR(100) NOT NULL                     │
│ deleted_at            TIMESTAMP                                 │
│ deleted_by            VARCHAR(100)                              │
└─────────────────────────────────────────────────────────────────┘
```

## 관련 서비스/프로젝트

| 서비스                  | 역할     |
|----------------------|--------|
| Product Service      | 상품 관리  |
| Reservation Service  | 예매 관리  |
| Notification Service | 알림 관리  |
| User Service         | 사용자 관리 |

---

© 2025 Tickatch Team