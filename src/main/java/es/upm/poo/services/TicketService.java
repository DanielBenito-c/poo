package es.upm.poo.services;

import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.repositories.ParkRepository;
import es.upm.poo.data.repositories.TicketRepository;
import es.upm.poo.data.repositories.UserRepository;

import java.util.List;
import java.util.Locale;

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
        String normalizedUserName = this.normalize(userName);
        String normalizedParkName = this.normalize(parkName);
        return this.ticketRepository.findAll().stream()
                .filter(ticket -> ticket.getUser() != null
                        && this.matches(ticket.getUser().getName(), normalizedUserName))
                .filter(ticket -> ticket.getPark() != null
                        && this.matches(ticket.getPark().getName(), normalizedParkName))
                .filter(ticket -> ticket.getTicketType() == ticketType)
                .toList();
    }

    private String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private boolean matches(String value, String normalizedExpected) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(normalizedExpected);
    }
}
