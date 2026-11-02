package es.upm.poo;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    @Test
    void testMainStartsApplicationAndExitsOnExitCommand() {
        ByteArrayInputStream input = new ByteArrayInputStream("exit".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        var originalInput = System.in;
        PrintStream originalOutput = System.out;

        try (PrintStream capturedOutput = new PrintStream(output, true, StandardCharsets.UTF_8)) {
            System.setIn(input);
            System.setOut(capturedOutput);
            App.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }

        assertTrue(output.toString(StandardCharsets.UTF_8).contains("Hasta pronto!"));
    }
}
