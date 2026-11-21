package es.upm.poo.presentation.cli;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandTest {

    @Test
    void testHelpFormatsObligatoryAndOptionalParams() {
        Command command = new Command() {
            @Override
            public String name() {
                return "demo";
            }

            @Override
            public List<String> obligatoryParams() {
                return List.of("required");
            }

            @Override
            public List<String> optionalParams() {
                return List.of("optional");
            }

            @Override
            public String helpMessage() {
                return "description";
            }

            @Override
            public void execute(CommandParams params) {
            }
        };

        assertEquals("demo#required,[optional]. description.", command.help());
    }

    @Test
    void testHelpWithoutParamsOmitsSeparator() {
        Command command = new Command() {
            @Override
            public String name() {
                return "demo";
            }

            @Override
            public List<String> obligatoryParams() {
                return List.of();
            }

            @Override
            public List<String> optionalParams() {
                return List.of();
            }

            @Override
            public String helpMessage() {
                return "description";
            }

            @Override
            public void execute(CommandParams params) {
            }
        };

        assertEquals("demo. description.", command.help());
    }

    @Test
    void testHelpWithOnlyOptionalParamsBracketsThem() {
        Command command = new Command() {
            @Override
            public String name() {
                return "demo";
            }

            @Override
            public List<String> obligatoryParams() {
                return List.of();
            }

            @Override
            public List<String> optionalParams() {
                return List.of("optional");
            }

            @Override
            public String helpMessage() {
                return "description";
            }

            @Override
            public void execute(CommandParams params) {
            }
        };

        assertEquals("demo#[optional]. description.", command.help());
    }
}
