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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketServiceIT {

    private static final LocalDate PURCHASE_DATE = LocalDate.of(2026, 1, 1);
    private static final LocalDate VALID_DATE = LocalDate.of(2026, 1, 2);

    private Ticket createTicket(User user, Park park) {
        return this.createTicket(user, park, Ticket.TicketType.STANDARD);
    }

    private Ticket createTicket(User user, Park park, Ticket.TicketType ticketType) {
        Ticket ticket = new Ticket(ticketType, new BigDecimal("50.00"), PURCHASE_DATE, VALID_DATE);
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

    @Test
    void testFindByUserNameAndParkNameAndTicketTypeFiltersAllCriteria() {
        UserRepositoryMap userRepository = new UserRepositoryMap();
        ParkRepositoryMap parkRepository = new ParkRepositoryMap();
        TicketService service = new TicketService(new TicketRepositoryMap(), userRepository, parkRepository);
        User alice = userRepository.create(new User("Alice Johnson", "alice@upm.es",
                LocalDate.of(1996, 1, 1), User.Gender.FEMALE, true));
        User marco = userRepository.create(new User("Marco Rossi", "marco@upm.es",
                LocalDate.of(1996, 1, 1), User.Gender.MALE, true));
        Park magicKingdom = parkRepository.create(new Park("Magic Kingdom", "Orlando",
                LocalTime.of(9, 0), LocalTime.of(22, 0), "Fantasyland", "Fantasy"));
        Park epcot = parkRepository.create(new Park("Epcot", "Orlando",
                LocalTime.of(9, 0), LocalTime.of(21, 0), "World Showcase", "Cultures"));

        Ticket matchingTicket = service.create(
                this.createTicket(alice, magicKingdom, Ticket.TicketType.STANDARD));
        service.create(this.createTicket(alice, magicKingdom, Ticket.TicketType.FAST_PASS));
        service.create(this.createTicket(alice, epcot, Ticket.TicketType.STANDARD));
        service.create(this.createTicket(marco, magicKingdom, Ticket.TicketType.STANDARD));

        List<Ticket> tickets = service.findByUserNameAndParkNameAndTicketType(
                "alice", "magic", Ticket.TicketType.STANDARD);

        assertEquals(1, tickets.size());
        assertSame(matchingTicket, tickets.get(0));
    }

    @Test
    void testFindByUserNameAndParkNameAndTicketTypeReturnsEmptyWhenAnyCriterionDoesNotMatch() {
        UserRepositoryMap userRepository = new UserRepositoryMap();
        ParkRepositoryMap parkRepository = new ParkRepositoryMap();
        TicketService service = new TicketService(new TicketRepositoryMap(), userRepository, parkRepository);
        User alice = userRepository.create(new User("Alice Johnson", "alice@upm.es",
                LocalDate.of(1996, 1, 1), User.Gender.FEMALE, true));
        Park magicKingdom = parkRepository.create(new Park("Magic Kingdom", "Orlando",
                LocalTime.of(9, 0), LocalTime.of(22, 0), "Fantasyland", "Fantasy"));
        service.create(this.createTicket(alice, magicKingdom, Ticket.TicketType.STANDARD));

        assertTrue(service.findByUserNameAndParkNameAndTicketType(
                "unknown", "magic", Ticket.TicketType.STANDARD).isEmpty());
        assertTrue(service.findByUserNameAndParkNameAndTicketType(
                "alice", "unknown", Ticket.TicketType.STANDARD).isEmpty());
        assertTrue(service.findByUserNameAndParkNameAndTicketType(
                "alice", "magic", Ticket.TicketType.FAST_PASS).isEmpty());
    }
}
