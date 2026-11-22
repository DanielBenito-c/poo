package es.upm.poo.data.repositories;

import es.upm.poo.data.model.Ticket;

import java.util.List;
import java.util.Map;

public interface TicketRepository extends GenericRepository<Ticket> {
    List<Ticket> findByUserNameAndParkNameAndTicketType(String userName, String parkName,
                                                        Ticket.TicketType ticketType);

    Map<String, Long> countVisitsByYear(int year);
}
