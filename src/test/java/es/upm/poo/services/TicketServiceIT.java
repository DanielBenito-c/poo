package es.upm.poo.services;

import es.upm.poo.data.model.Park;
import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.map.ParkRepositoryMap;
import es.upm.poo.data.repositories.map.TicketRepositoryMap;
import es.upm.poo.data.repositories.map.UserRepositoryMap;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class TicketServiceIT {

    private static final LocalDate PURCHASE_DATE = LocalDate.of(2026, 1, 1);
    private static final LocalDate VALID_DATE = LocalDate.of(2026, 1, 2);

    private Ticket createTicket(User user, Park park) {
        Ticket ticket = new Ticket(Ticket.TicketType.STANDARD, new BigDecimal("50.00"),
                PURCHASE_DATE, VALID_DATE);
        ticket.setUser(user);
        ticket.setPark(park);
        return ticket;
    }

    @Test
    void testCreatePersistsTicketAndAssignsId() {
        UserRepositoryMap userRepository = new UserRepositoryMap();
        ParkRepositoryMap parkRepository = new ParkRepositoryMap();
        TicketRepositoryMap ticketRepository = new TicketRepositoryMap();
        TicketService service = new TicketService(ticketRepository, userRepository, parkRepository);
        User user = userRepository.create(new User("Alice", "alice@upm.es",
                LocalDate.of(1996, 1, 1), User.Gender.FEMALE, true));
        Park park = parkRepository.create(new Park("Park", "Address", LocalTime.of(9, 0),
                LocalTime.of(20, 0), "Zone", "Theme"));

        Ticket createdTicket = service.create(this.createTicket(user, park));

        assertEquals(1L, createdTicket.getId());
        assertEquals(Ticket.TicketType.STANDARD, createdTicket.getTicketType());
        assertEquals(new BigDecimal("50.00"), createdTicket.getPrice());
        assertEquals(PURCHASE_DATE, createdTicket.getPurchaseDate());
        assertEquals(VALID_DATE, createdTicket.getValidDate());
        assertSame(user, createdTicket.getUser());
        assertSame(park, createdTicket.getPark());
        assertSame(createdTicket, ticketRepository.read(1L).orElseThrow());
    }

    @Test
    void testFindAllReturnsCreatedTickets() {
        UserRepositoryMap userRepository = new UserRepositoryMap();
        ParkRepositoryMap parkRepository = new ParkRepositoryMap();
        TicketService service = new TicketService(new TicketRepositoryMap(), userRepository, parkRepository);
        User user = userRepository.create(new User("Alice", "alice@upm.es",
                LocalDate.of(1996, 1, 1), User.Gender.FEMALE, true));
        Park park = parkRepository.create(new Park("Park", "Address", LocalTime.of(9, 0),
                LocalTime.of(20, 0), "Zone", "Theme"));

        service.create(this.createTicket(user, park));
        service.create(this.createTicket(user, park));

        assertEquals(2, service.findAll().size());
    }
}
