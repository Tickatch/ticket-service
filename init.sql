CREATE SCHEMA ticket_service;

-- ============================
-- ENUM 타입 생성
-- ============================

-- ============================
-- 티켓 테이블 생성
-- ============================
DROP TABLE IF EXISTS p_ticket;

CREATE TABLE p_ticket (
                          id              UUID                NOT NULL PRIMARY KEY,
                          reservation_id  UUID                NOT NULL,
                          product_id      BIGINT              NOT NULL,
                          seat_id         BIGINT              NOT NULL,
                          seat_number     VARCHAR(20)         NOT NULL,
                          grade           VARCHAR(20)         NOT NULL,
                          price           BIGINT              NOT NULL,
                          receive_method  VARCHAR(20) NOT NULL,
                          status          VARCHAR(20)  NOT NULL,
                          issued_at       TIMESTAMP,
                          used_at         TIMESTAMP,
                          created_at      TIMESTAMP           NOT NULL,
                          created_by      VARCHAR(100)        NOT NULL,
                          updated_at      TIMESTAMP           NOT NULL,
                          updated_by      VARCHAR(100)        NOT NULL,
                          deleted_at      TIMESTAMP,
                          deleted_by      VARCHAR(100)
);