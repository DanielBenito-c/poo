package es.upm.poo.presentation.cli.commands;

import es.upm.poo.configuration.DependencyInjector;
import es.upm.poo.presentation.cli.CommandLineInterface;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FindUserByEmailIT {

    private static final String SEEDED_EMAIL = "alice.johnson@upm.es";

    @Test
    void testCommandIsRegisteredAndDisplaysMatchingUser() {
        CommandLineInterface commandLineInterface =
                DependencyInjector.getInstance().getCommandLineInterface();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try (PrintStream capturedOutput = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(capturedOutput);

            boolean shouldExit = commandLineInterface.runCommand(
                    new Scanner("find-user-by-email#" + SEEDED_EMAIL)
                            .useDelimiter("[#\\r\\n]"));

            assertFalse(shouldExit);
        } finally {
            System.setOut(originalOutput);
        }

        assertTrue(output.toString(StandardCharsets.UTF_8).contains(SEEDED_EMAIL));
    }
}
