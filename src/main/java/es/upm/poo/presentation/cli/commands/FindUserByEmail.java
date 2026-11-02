package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.User;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.UserService;

import java.util.List;

public class FindUserByEmail implements Command {
    private final View view;
    private final UserService userService;

    public FindUserByEmail(View view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    @Override
    public String name() {
        return "find-user-by-email";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("email");
    }

    @Override
    public List<String> optionalParams() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Devuelve un usuario por su email";
    }

    @Override
    public void execute(CommandParams params) {
        User foundUser = this.userService.findUserByEmail(params.getString(0));
        this.view.showItem(foundUser);
    }
}
