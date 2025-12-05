package com.tickatch.ticketservice.ticket.domain.repository;

import com.tickatch.ticketservice.ticket.domain.Ticket;
import com.tickatch.ticketservice.ticket.domain.TicketId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, TicketId> {

  // 예매 id로 발행된 티켓 조회
  Optional<Ticket> findByReservationId(UUID reservationId);
}
