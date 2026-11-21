package es.upm.poo.presentation.cli;

import es.upm.poo.data.model.Consent;
import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.ConsentRepository;
import es.upm.poo.data.repositories.UserRepository;
import es.upm.poo.data.repositories.map.ConsentRepositoryMap;
import es.upm.poo.data.repositories.map.UserRepositoryMap;
import es.upm.poo.presentation.cli.commands.CreateConsent;
import es.upm.poo.presentation.cli.commands.CreateUser;
import es.upm.poo.presentation.cli.commands.DeleteUser;
import es.upm.poo.presentation.cli.commands.Exit;
import es.upm.poo.presentation.cli.commands.FindAllUsers;
import es.upm.poo.presentation.cli.commands.FindUserByEmail;
import es.upm.poo.presentation.cli.commands.Help;
import es.upm.poo.presentation.cli.commands.ReadUser;
import es.upm.poo.presentation.exceptions.BadRequestException;
import es.upm.poo.presentation.exceptions.CommandException;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.ConsentService;
import es.upm.poo.services.UserService;
import es.upm.poo.services.exceptions.ConflictException;
import es.upm.poo.services.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandLineInterfaceIT {

    private TestContext createContext() {
        View view = new View();
        UserRepository userRepository = new UserRepositoryMap();
        UserService userService = new UserService(userRepository);
        ConsentRepository consentRepository = new ConsentRepositoryMap();
        ConsentService consentService = new ConsentService(userRepository, consentRepository);
        CommandLineInterface commandLineInterface = new CommandLineInterface(view);
        commandLineInterface.add(new Help(commandLineInterface));
        commandLineInterface.add(new Exit());
        commandLineInterface.add(new CreateUser(view, userService));
        commandLineInterface.add(new FindAllUsers(view, userService));
        commandLineInterface.add(new ReadUser(view, userService));
        commandLineInterface.add(new FindUserByEmail(view, userService));
        commandLineInterface.add(new DeleteUser(view, userService));
        commandLineInterface.add(new CreateConsent(view, consentService, userService));
        return new TestContext(commandLineInterface, userService, consentRepository);
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

    private User createUser(UserService userService) {
        return userService.create(new User("Alice Johnson", "alice@upm.es",
                LocalDate.of(1996, 1, 1), User.Gender.FEMALE, true));
    }

    @Test
    void testCreateUserCommandCreatesUserAndDisplaysIt() {
        TestContext context = this.createContext();

        String output = this.runCommandAndCapture(context.commandLineInterface(),
                "create-user#Alice,alice@upm.es,1996-01-01,FEMALE,true");

        assertEquals(1, context.userService().findAll().size());
        assertTrue(output.contains("alice@upm.es"));
    }

    @Test
    void testCreateUserCommandUsesDefaultWhenOptionalActiveIsOmitted() {
        TestContext context = this.createContext();

        this.runCommandAndCapture(context.commandLineInterface(),
                "create-user#Alice,alice@upm.es,1996-01-01,FEMALE");

        assertTrue(context.userService().findAll().get(0).isActive());
    }

    @Test
    void testFindAllUsersCommandDisplaysStoredUsers() {
        TestContext context = this.createContext();
        this.createUser(context.userService());

        String output = this.runCommandAndCapture(context.commandLineInterface(), "find-all-users");

        assertTrue(output.contains("alice@upm.es"));
    }

    @Test
    void testReadUserCommandDisplaysRequestedUser() {
        TestContext context = this.createContext();
        this.createUser(context.userService());

        String output = this.runCommandAndCapture(context.commandLineInterface(), "read-user#1");

        assertTrue(output.contains("alice@upm.es"));
    }

    @Test
    void testDeleteUserCommandRemovesRequestedUser() {
        TestContext context = this.createContext();
        this.createUser(context.userService());

        this.runCommandAndCapture(context.commandLineInterface(), "delete-user#1");

        assertTrue(context.userService().findAll().isEmpty());
    }

    @Test
    void testCreateConsentCommandPersistsConsentForExistingUserAndDisplaysIt() {
        TestContext context = this.createContext();
        User user = this.createUser(context.userService());

        String output = this.runCommandAndCapture(context.commandLineInterface(),
                "create-consent#" + user.getId() + ",web,BASIC");

        Consent consent = context.consentRepository().read(1L).orElseThrow();
        assertEquals(1L, consent.getId());
        assertSame(user, consent.getUser());
        assertNotNull(consent.getGrantedAt());
        assertTrue(output.contains("userId=" + user.getId()));
    }

    @Test
    void testCreateConsentCommandRejectsUnknownUser() {
        TestContext context = this.createContext();

        assertThrows(NotFoundException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(), "create-consent#99,web,BASIC"));
    }

    @Test
    void testHelpCommandIncludesRegisteredCommands() {
        TestContext context = this.createContext();

        String output = this.runCommandAndCapture(context.commandLineInterface(), "help");

        assertTrue(output.contains("create-user"));
        assertTrue(output.contains("find-user-by-email"));
        assertTrue(output.contains("create-consent"));
        assertTrue(output.contains("create-user#name,email,dateOfBirth,gender,[active]"));
        assertTrue(output.contains("create-consent#userId,source,purpose,[grantedAt]"));
    }

    @Test
    void testExitCommandSignalsExit() {
        TestContext context = this.createContext();
        PrintStream originalOutput = System.out;

        try (PrintStream capturedOutput = new PrintStream(new ByteArrayOutputStream())) {
            System.setOut(capturedOutput);

            boolean shouldExit = context.commandLineInterface()
                    .runCommand(new Scanner("exit").useDelimiter("[#\\r\\n]"));

            assertTrue(shouldExit);
        } finally {
            System.setOut(originalOutput);
        }
    }

    @Test
    void testUnknownCommandThrowsCommandException() {
        TestContext context = this.createContext();

        assertThrows(CommandException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(), "unknown"));
    }

    @Test
    void testCommandWithTooManyParametersThrowsBadRequestException() {
        TestContext context = this.createContext();

        assertThrows(BadRequestException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(), "read-user#1,2"));
    }

    @Test
    void testCommandWithoutRequiredParametersThrowsBadRequestException() {
        TestContext context = this.createContext();

        assertThrows(BadRequestException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(), "create-user#Alice"));
    }

    @Test
    void testFindUserByEmailCommandDisplaysMatchingUser() {
        TestContext context = this.createContext();
        this.createUser(context.userService());

        String output = this.runCommandAndCapture(context.commandLineInterface(),
                "find-user-by-email#ALICE@UPM.ES");

        assertTrue(output.contains("alice@upm.es"));
    }

    @Test
    void testCreateUserCommandRejectsDuplicatedEmail() {
        TestContext context = this.createContext();
        this.createUser(context.userService());

        ConflictException exception = assertThrows(ConflictException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(),
                        "create-user#Another,alice@upm.es,1996-01-01,FEMALE"));

        assertTrue(exception.getMessage().contains("alice@upm.es"));
        assertEquals(1, context.userService().findAll().size());
    }

    @Test
    void testReadUserCommandRejectsUnknownUser() {
        TestContext context = this.createContext();

        assertThrows(NotFoundException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(), "read-user#99"));
    }

    @Test
    void testFindUserByEmailCommandRejectsUnknownEmail() {
        TestContext context = this.createContext();

        assertThrows(NotFoundException.class,
                () -> this.runCommandAndCapture(context.commandLineInterface(),
                        "find-user-by-email#unknown@upm.es"));
    }

    @Test
    void testFindAllUsersCommandReportsNoItemsWhenEmpty() {
        TestContext context = this.createContext();

        String output = this.runCommandAndCapture(context.commandLineInterface(), "find-all-users");

        assertTrue(output.contains("No items available"));
    }

    @Test
    void testDeleteUserCommandWithUnknownIdKeepsExistingUser() {
        TestContext context = this.createContext();
        this.createUser(context.userService());

        this.runCommandAndCapture(context.commandLineInterface(), "delete-user#99");

        assertEquals(1, context.userService().findAll().size());
    }

    private record TestContext(CommandLineInterface commandLineInterface,
                               UserService userService,
                               ConsentRepository consentRepository) {
    }
}
