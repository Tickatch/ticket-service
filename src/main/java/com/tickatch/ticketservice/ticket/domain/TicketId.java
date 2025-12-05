package com.tickatch.ticketservice.ticket.domain;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TicketId {

  private UUID id;

  private TicketId(UUID id){
    this.id = id;
  }

  public static TicketId of(){
    return of(null);
  }

  public static TicketId of(UUID id){
    id = Objects.requireNonNullElse(id, UUID.randomUUID());
    return new TicketId(id);
  }

  public UUID toUuid() {
    return id;
  }

  @Override
  public String toString() {
    return id.toString();
  }
}
