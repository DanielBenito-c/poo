package es.upm.poo.presentation;

import es.upm.poo.presentation.cli.CommandLineInterface;
import es.upm.poo.presentation.exceptions.CommandException;
import es.upm.poo.presentation.view.View;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorHandlerTest {

    private static class StubCommandLineInterface extends CommandLineInterface {
        private int calls;

        private StubCommandLineInterface() {
            super(new View());
        }

        @Override
        public boolean runCommands() {
            this.calls++;
            if (this.calls == 1) {
                throw new CommandException("boom");
            }
            return true;
        }
    }

    @Test
    void testHandlesErrorsShowsErrorAndKeepsRunningUntilExit() {
        ErrorHandler errorHandler = new ErrorHandler();
        StubCommandLineInterface commandLineInterface = new StubCommandLineInterface();
        View view = new View();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try (PrintStream capturedOutput = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setOut(capturedOutput);
            errorHandler.handlesErrors(commandLineInterface, view);
        } finally {
            System.setOut(originalOutput);
        }

        String text = output.toString(StandardCharsets.UTF_8);
        assertTrue(text.contains("App UPM"));
        assertTrue(text.contains("ERROR (CommandException)"));
        assertTrue(text.contains("boom"));
        assertTrue(text.contains("Hasta pronto!"));
        assertEquals(2, commandLineInterface.calls);
    }
}
