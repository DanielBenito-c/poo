package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.User;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.UserService;

import java.util.List;

public class FindUsersByName implements Command {
    private final View view;
    private final UserService userService;

    public FindUsersByName(View view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    @Override
    public String name() {
        return "find-users-by-name";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("name");
    }

    @Override
    public List<String> optionalParams() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Devuelve los usuarios cuyo nombre contiene el texto indicado";
    }

    @Override
    public void execute(CommandParams params) {
        List<User> users = this.userService.findUsersByName(params.getString(0));
        this.view.showList("Users", users);
    }
}
