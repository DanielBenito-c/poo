package es.upm.poo.presentation.cli.commands;

import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.UserService;

import java.util.List;

public class DeleteUser implements Command {
    private final View view;
    private final UserService userService;

    public DeleteUser(View view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    @Override
    public String name() {
        return "delete-user";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("id");
    }

    @Override
    public List<String> optionalParams() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Borra un usuario por su id";
    }

    @Override
    public void execute(CommandParams params) {
        Long id = params.getLong(0);
        this.userService.readUser(id);
        this.userService.deleteUser(id);
        this.view.show("Usuario eliminado con id: " + id);
    }
}
