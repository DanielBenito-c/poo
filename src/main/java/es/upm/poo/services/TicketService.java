package es.upm.poo.services;

import es.upm.poo.data.repositories.ParkRepository;
import es.upm.poo.data.repositories.TicketRepository;
import es.upm.poo.data.repositories.UserRepository;

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
}
