package es.upm.poo.services;

import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.repositories.ParkRepository;
import es.upm.poo.data.repositories.TicketRepository;
import es.upm.poo.data.repositories.UserRepository;

import java.util.List;

public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ParkRepository parkRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository,
                         ParkRepository parkRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.parkRepository = parkRepository;
    }

    public Ticket create(Ticket ticket) {
        return this.ticketRepository.create(ticket);
    }

    public List<Ticket> findAll() {
        return this.ticketRepository.findAll();
    }

    public List<Ticket> findByUserNameAndParkNameAndTicketType(String userName, String parkName,
                                                               Ticket.TicketType ticketType) {
        return this.ticketRepository.findByUserNameAndParkNameAndTicketType(
                userName, parkName, ticketType);
    }

    public List<VisitCount> countVisitsByYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("year cannot be negative: " + year);
        }
        return this.ticketRepository.countVisitsByYear(year).entrySet().stream()
                .map(entry -> new VisitCount(entry.getKey(), year, entry.getValue()))
                .toList();
    }
}
