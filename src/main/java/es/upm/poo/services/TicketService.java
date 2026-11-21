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
}
