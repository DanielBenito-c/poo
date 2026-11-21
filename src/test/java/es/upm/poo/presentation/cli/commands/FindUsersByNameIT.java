package es.upm.poo.presentation.cli.commands;

import es.upm.poo.configuration.DependencyInjector;
import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.map.UserRepositoryMap;
import es.upm.poo.presentation.cli.CommandLineInterface;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.UserService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FindUsersByNameIT {

    private static final String SEEDED_NAME = "alice";
    private static final String SEEDED_EMAIL = "alice.johnson@upm.es";

    private TestContext createContext() {
        View view = new View();
        UserService userService = new UserService(new UserRepositoryMap());
        CommandLineInterface commandLineInterface = new CommandLineInterface(view);
        commandLineInterface.add(new FindUsersByName(view, userService));
        return new TestContext(commandLineInterface, userService);
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

    private User createUser(UserService userService, String name, String email) {
        return userService.create(new User(name, email, LocalDate.of(1996, 1, 1),
                User.Gender.FEMALE, true));
    }

    @Test
    void testCommandIsRegisteredAndDisplaysMatchingUser() {
        CommandLineInterface commandLineInterface =
                DependencyInjector.getInstance().getCommandLineInterface();

        String output = this.runCommandAndCapture(commandLineInterface,
                "find-users-by-name#" + SEEDED_NAME);

        assertTrue(output.contains(SEEDED_EMAIL));
    }

    @Test
    void testCommandDisplaysAllUsersWhoseNameContainsTextIgnoringCase() {
        TestContext context = this.createContext();
        this.createUser(context.userService(), "Alice Johnson", "alice@upm.es");
        this.createUser(context.userService(), "Alicia Keys", "alicia@upm.es");
        this.createUser(context.userService(), "Marco Rossi", "marco@upm.es");

        String output = this.runCommandAndCapture(context.commandLineInterface(),
                "find-users-by-name#ALI");

        assertTrue(output.contains("alice@upm.es"));
        assertTrue(output.contains("alicia@upm.es"));
        assertFalse(output.contains("marco@upm.es"));
    }

    @Test
    void testCommandDisplaysNoItemsWhenNoUserMatches() {
        TestContext context = this.createContext();
        this.createUser(context.userService(), "Alice Johnson", "alice@upm.es");

        String output = this.runCommandAndCapture(context.commandLineInterface(),
                "find-users-by-name#Unknown");

        assertTrue(output.contains("No items available"));
    }

    private record TestContext(CommandLineInterface commandLineInterface, UserService userService) {
    }
}
