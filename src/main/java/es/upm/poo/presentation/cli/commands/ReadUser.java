package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.User;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.UserService;

import java.util.List;
import java.util.Optional;

public class ReadUser implements Command {
    private final View view;
    private final UserService userService;

    public ReadUser(View view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    @Override
    public String name() {
        return "read-user";
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
        return "Devuelve un usuario por su id";
    }

    @Override
    public void execute(CommandParams params) {
        Optional<User> userFindById = this.userService.readUser(params.getLong(0));
        this.view.showItem(userFindById);
    }
}
