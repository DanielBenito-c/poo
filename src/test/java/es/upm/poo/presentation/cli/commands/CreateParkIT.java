package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Park;
import es.upm.poo.data.repositories.map.ParkRepositoryMap;
import es.upm.poo.presentation.cli.CommandLineInterface;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.ParkService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateParkIT {

    private static final String COMMAND = "create-park#Park,Address,09:00,20:00,Zone,Theme";

    private TestContext createContext() {
        View view = new View();
        ParkRepositoryMap parkRepository = new ParkRepositoryMap();
        ParkService parkService = new ParkService(parkRepository);
        CommandLineInterface commandLineInterface = new CommandLineInterface(view);
        commandLineInterface.add(new CreatePark(view, parkService));
        commandLineInterface.add(new FindAllParks(view, parkService));
        return new TestContext(commandLineInterface, parkRepository);
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
    void testCreateParkCommandPersistsParkAndDisplaysIt() {
        TestContext context = this.createContext();

        String output = this.runCommandAndCapture(context.commandLineInterface(), COMMAND);

        Park park = context.parkRepository().read(1L).orElseThrow();
        assertEquals("Park", park.getName());
        assertEquals("Address", park.getAddress());
        assertEquals(LocalTime.of(9, 0), park.getOpeningTime());
        assertEquals(LocalTime.of(20, 0), park.getClosingTime());
        assertEquals("Zone", park.getZones().get(0).getName());
        assertTrue(output.contains("name=Park"));
        assertTrue(output.contains("zones=[Zone]"));
    }

    @Test
    void testFindAllParksCommandDisplaysStoredParks() {
        TestContext context = this.createContext();
        this.runCommandAndCapture(context.commandLineInterface(), COMMAND);

        String output = this.runCommandAndCapture(context.commandLineInterface(), "find-all-parks");

        assertTrue(output.contains("Parks"));
        assertTrue(output.contains("name=Park"));
    }

    private record TestContext(CommandLineInterface commandLineInterface,
                               ParkRepositoryMap parkRepository) {
    }
}
