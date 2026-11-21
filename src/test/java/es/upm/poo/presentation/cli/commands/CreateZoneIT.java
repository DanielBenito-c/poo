package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Park;
import es.upm.poo.data.repositories.map.ParkRepositoryMap;
import es.upm.poo.data.repositories.map.ZoneRepositoryMap;
import es.upm.poo.presentation.cli.CommandLineInterface;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.ParkService;
import es.upm.poo.services.ZoneService;
import es.upm.poo.services.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateZoneIT {

    private static final String COMMAND = "create-zone#1,Aventura,Emociones";

    private TestContext createContext() {
        View view = new View();
        ParkRepositoryMap parkRepository = new ParkRepositoryMap();
        ParkService parkService = new ParkService(parkRepository);
        parkRepository.create(new Park("Park", "Address", LocalTime.of(9, 0),
                LocalTime.of(20, 0), "Initial", "Theme"));
        ZoneRepositoryMap zoneRepository = new ZoneRepositoryMap();
        ZoneService zoneService = new ZoneService(zoneRepository, parkRepository);
        CommandLineInterface commandLineInterface = new CommandLineInterface(view);
        commandLineInterface.add(new CreateZone(view, zoneService, parkService));
        commandLineInterface.add(new FindAllZones(view, zoneService));
        return new TestContext(commandLineInterface, parkRepository, zoneRepository);
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
    void testCreateZoneCommandPersistsZoneAndAddsItToPark() {
        TestContext context = this.createContext();

        String output = this.runCommandAndCapture(context.commandLineInterface(), COMMAND);

        assertEquals("Aventura", context.zoneRepository().read(1L).orElseThrow().getName());
        Park park = context.parkRepository().read(1L).orElseThrow();
        assertEquals(2, park.getZones().size());
        assertEquals("Aventura", park.getZones().get(1).getName());
        assertTrue(output.contains("name=Aventura"));
    }

    @Test
    void testCreateZoneCommandRejectsUnknownPark() {
        TestContext context = this.createContext();

        assertThrows(NotFoundException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(),
                        "create-zone#99,Aventura,Emociones"));
    }

    @Test
    void testCreateZoneCommandRejectsBlankTheme() {
        TestContext context = this.createContext();

        assertThrows(IllegalArgumentException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(),
                        "create-zone#1,Aventura,   "));

        assertTrue(context.zoneRepository().findAll().isEmpty());
        assertEquals(1, context.parkRepository().read(1L).orElseThrow().getZones().size());
    }

    @Test
    void testFindAllZonesCommandDisplaysStoredZones() {
        TestContext context = this.createContext();
        this.runCommandAndCapture(context.commandLineInterface(), COMMAND);

        String output = this.runCommandAndCapture(context.commandLineInterface(), "find-all-zones");

        assertTrue(output.contains("Zones"));
        assertTrue(output.contains("name=Aventura"));
    }

    private record TestContext(CommandLineInterface commandLineInterface,
                               ParkRepositoryMap parkRepository,
                               ZoneRepositoryMap zoneRepository) {
    }
}
