package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Attraction;
import es.upm.poo.data.model.Zone;
import es.upm.poo.data.repositories.map.AttractionRepositoryMap;
import es.upm.poo.data.repositories.map.ParkRepositoryMap;
import es.upm.poo.data.repositories.map.ZoneRepositoryMap;
import es.upm.poo.presentation.cli.CommandLineInterface;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.AttractionService;
import es.upm.poo.services.ZoneService;
import es.upm.poo.services.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateAttractionIT {

    private static final String COMMAND = "create-attraction#1,Thunder,50,120";

    private TestContext createContext() {
        View view = new View();
        ZoneRepositoryMap zoneRepository = new ZoneRepositoryMap();
        ZoneService zoneService = new ZoneService(zoneRepository, new ParkRepositoryMap());
        zoneService.create(new Zone("Aventura", "Emociones"));
        AttractionRepositoryMap attractionRepository = new AttractionRepositoryMap();
        AttractionService attractionService = new AttractionService(attractionRepository, zoneRepository);
        CommandLineInterface commandLineInterface = new CommandLineInterface(view);
        commandLineInterface.add(new CreateAttraction(view, attractionService, zoneService));
        commandLineInterface.add(new FindAllAttractions(view, attractionService));
        return new TestContext(commandLineInterface, zoneRepository, attractionRepository);
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
    void testCreateAttractionCommandPersistsAttractionAndAddsItToZone() {
        TestContext context = this.createContext();

        String output = this.runCommandAndCapture(context.commandLineInterface(), COMMAND);

        Attraction attraction = context.attractionRepository().read(1L).orElseThrow();
        assertEquals("Thunder", attraction.getName());
        assertEquals(120, attraction.getMinHeightCm());
        assertEquals(50, attraction.getCapacity());
        Zone zone = context.zoneRepository().read(1L).orElseThrow();
        assertEquals(1, zone.getAttractions().size());
        assertTrue(output.contains("name=Thunder"));
    }

    @Test
    void testCreateAttractionCommandAllowsMissingMinHeight() {
        TestContext context = this.createContext();

        this.runCommandAndCapture(context.commandLineInterface(), "create-attraction#1,Carousel,30");

        assertNull(context.attractionRepository().read(1L).orElseThrow().getMinHeightCm());
    }

    @Test
    void testCreateAttractionCommandRejectsUnknownZone() {
        TestContext context = this.createContext();

        assertThrows(NotFoundException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(),
                        "create-attraction#99,Thunder,50,120"));
    }

    @Test
    void testFindAllAttractionsCommandDisplaysStoredAttractions() {
        TestContext context = this.createContext();
        this.runCommandAndCapture(context.commandLineInterface(), COMMAND);

        String output = this.runCommandAndCapture(context.commandLineInterface(),
                "find-all-attractions");

        assertTrue(output.contains("Attractions"));
        assertTrue(output.contains("name=Thunder"));
    }

    private record TestContext(CommandLineInterface commandLineInterface,
                               ZoneRepositoryMap zoneRepository,
                               AttractionRepositoryMap attractionRepository) {
    }
}
