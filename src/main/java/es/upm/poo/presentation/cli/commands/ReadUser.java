package es.upm.poo.presentation.cli.commands;

import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.UserService;

import java.util.List;

public class ReadUser implements Command {
    private final View view;
    private final UserService userService;

    public ReadUser(View view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    @Override
    public String name() {
        return "read-user#<id>";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("name", "email", "dateOfBirth", "gender");
    }
}
