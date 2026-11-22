package es.upm.poo.presentation.cli.commands;

import es.upm.poo.configuration.DependencyInjector;
import es.upm.poo.data.model.Park;
import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.map.ParkRepositoryMap;
import es.upm.poo.data.repositories.map.TicketRepositoryMap;
import es.upm.poo.data.repositories.map.UserRepositoryMap;
import es.upm.poo.presentation.cli.CommandLineInterface;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.TicketService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FindTicketsByUserParkTypeIT {

    private static final String COMMAND = "find-tickets-by-user-park-type#";
    private static final LocalDate PURCHASE_DATE = LocalDate.of(2026, 1, 1);
    private static final LocalDate VALID_DATE = LocalDate.of(2026, 1, 2);

    private TestContext createContext() {
        View view = new View();
        UserRepositoryMap userRepository = new UserRepositoryMap();
        ParkRepositoryMap parkRepository = new ParkRepositoryMap();
        TicketRepositoryMap ticketRepository = new TicketRepositoryMap();
        TicketService ticketService = new TicketService(ticketRepository, userRepository, parkRepository);
        CommandLineInterface commandLineInterface = new CommandLineInterface(view);
        commandLineInterface.add(new FindTicketsByUserParkType(view, ticketService));
        return new TestContext(commandLineInterface, userRepository, parkRepository, ticketService);
    }

    private String runCommandAndCapture(CommandLineInterface commandLineInterface, String input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try (PrintStream capturedOutput = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(capturedOutput);
            commandLineInterface.runCommand(new Scanner(input).useDelimiter("[#\\r\\n]"));
        } finally {
            System.setOut(originalOutput);
        }
        return output.toString(StandardCharsets.UTF_8);
    }

    private User createUser(TestContext context, String name, String email) {
        return context.userRepository().create(new User(name, email, LocalDate.of(1996, 1, 1),
                User.Gender.FEMALE, true));
    }

    private Park createPark(TestContext context, String name) {
        return context.parkRepository().create(new Park(name, "Address", LocalTime.of(9, 0),
                LocalTime.of(20, 0), "Zone", "Theme"));
    }

    private Ticket createTicket(TestContext context, User user, Park park, Ticket.TicketType ticketType) {
        Ticket ticket = new Ticket(ticketType, new BigDecimal("50.00"), PURCHASE_DATE, VALID_DATE);
        ticket.setUser(user);
        ticket.setPark(park);
        return context.ticketService().create(ticket);
    }

    @Test
    void testCommandIsRegisteredAndDisplaysMatchingSeededTicket() {
        CommandLineInterface commandLineInterface =
                DependencyInjector.getInstance().getCommandLineInterface();

        String output = this.runCommandAndCapture(commandLineInterface,
                COMMAND + "alice,Magic Kingdom,STANDARD");

        assertTrue(output.contains("ticketType=STANDARD"));
        assertTrue(output.contains("price=55.00"));
    }

    @Test
    void testCommandDisplaysOnlyTicketsMatchingAllCriteria() {
        TestContext context = this.createContext();
        User alice = this.createUser(context, "Alice Johnson", "alice@upm.es");
        User marco = this.createUser(context, "Marco Rossi", "marco@upm.es");
        Park magicKingdom = this.createPark(context, "Magic Kingdom");
        Park epcot = this.createPark(context, "Epcot");

        this.createTicket(context, alice, magicKingdom, Ticket.TicketType.STANDARD);
        this.createTicket(context, alice, magicKingdom, Ticket.TicketType.FAST_PASS);
        this.createTicket(context, alice, epcot, Ticket.TicketType.STANDARD);
        this.createTicket(context, marco, magicKingdom, Ticket.TicketType.STANDARD);

        String output = this.runCommandAndCapture(context.commandLineInterface(),
                COMMAND + "alice,magic,STANDARD");

        assertTrue(output.contains("Tickets"));
        assertTrue(output.contains("Ticket{id=1"));
        assertFalse(output.contains("Ticket{id=2"));
        assertFalse(output.contains("Ticket{id=3"));
        assertFalse(output.contains("Ticket{id=4"));
    }

    @Test
    void testCommandDisplaysNoItemsWhenNoTicketMatches() {
        TestContext context = this.createContext();
        User alice = this.createUser(context, "Alice Johnson", "alice@upm.es");
        Park magicKingdom = this.createPark(context, "Magic Kingdom");
        this.createTicket(context, alice, magicKingdom, Ticket.TicketType.STANDARD);

        String output = this.runCommandAndCapture(context.commandLineInterface(),
                COMMAND + "unknown,Magic Kingdom,STANDARD");

        assertTrue(output.contains("No items available"));
    }

    private record TestContext(CommandLineInterface commandLineInterface,
                               UserRepositoryMap userRepository,
                               ParkRepositoryMap parkRepository,
                               TicketService ticketService) {
    }
}
