package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Park;
import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.map.ParkRepositoryMap;
import es.upm.poo.data.repositories.map.TicketRepositoryMap;
import es.upm.poo.data.repositories.map.UserRepositoryMap;
import es.upm.poo.presentation.cli.CommandLineInterface;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.ParkService;
import es.upm.poo.services.TicketService;
import es.upm.poo.services.UserService;
import es.upm.poo.services.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateTicketIT {

    private static final String COMMAND = "create-ticket#1,1,STANDARD,50.00,2026-01-01,2026-01-02";

    private TestContext createContext() {
        View view = new View();
        UserRepositoryMap userRepository = new UserRepositoryMap();
        UserService userService = new UserService(userRepository);
        userService.create(new User("Alice", "alice@upm.es", LocalDate.of(1996, 1, 1),
                User.Gender.FEMALE, true));
        ParkRepositoryMap parkRepository = new ParkRepositoryMap();
        ParkService parkService = new ParkService(parkRepository);
        parkRepository.create(new Park("Park", "Address", LocalTime.of(9, 0),
                LocalTime.of(20, 0), "Zone", "Theme"));
        TicketRepositoryMap ticketRepository = new TicketRepositoryMap();
        TicketService ticketService = new TicketService(ticketRepository, userRepository, parkRepository);
        CommandLineInterface commandLineInterface = new CommandLineInterface(view);
        commandLineInterface.add(new CreateTicket(view, ticketService, userService, parkService));
        commandLineInterface.add(new FindAllTickets(view, ticketService));
        return new TestContext(commandLineInterface, ticketRepository);
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

    @Test
    void testCreateTicketCommandPersistsTicketAndDisplaysIt() {
        TestContext context = this.createContext();

        String output = this.runCommandAndCapture(context.commandLineInterface(), COMMAND);

        Ticket ticket = context.ticketRepository().read(1L).orElseThrow();
        assertEquals(Ticket.TicketType.STANDARD, ticket.getTicketType());
        assertEquals(1L, ticket.getUser().getId());
        assertEquals(1L, ticket.getPark().getId());
        assertTrue(output.contains("ticketType=STANDARD"));
    }

    @Test
    void testCreateTicketCommandRejectsUnknownUser() {
        TestContext context = this.createContext();

        assertThrows(NotFoundException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(),
                        "create-ticket#99,1,STANDARD,50.00,2026-01-01,2026-01-02"));
    }

    @Test
    void testCreateTicketCommandRejectsUnknownPark() {
        TestContext context = this.createContext();

        assertThrows(NotFoundException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(),
                        "create-ticket#1,99,STANDARD,50.00,2026-01-01,2026-01-02"));
    }

    @Test
    void testFindAllTicketsCommandDisplaysStoredTickets() {
        TestContext context = this.createContext();
        this.runCommandAndCapture(context.commandLineInterface(), COMMAND);

        String output = this.runCommandAndCapture(context.commandLineInterface(), "find-all-tickets");

        assertTrue(output.contains("Tickets"));
        assertTrue(output.contains("ticketType=STANDARD"));
    }

    private record TestContext(CommandLineInterface commandLineInterface,
                               TicketRepositoryMap ticketRepository) {
    }
}
