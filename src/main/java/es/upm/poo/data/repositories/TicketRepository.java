package es.upm.poo.data.repositories;

import es.upm.poo.data.model.Ticket;

import java.util.List;

public interface TicketRepository extends GenericRepository<Ticket> {
    List<Ticket> findByUserNameAndParkNameAndTicketType(String userName, String parkName,
                                                        Ticket.TicketType ticketType);

    List<String> countVisitsByYear(int year);
}
