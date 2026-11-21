package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Park;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.ParkService;

import java.util.List;

public class FindAllParks implements Command {
    private final View view;
    private final ParkService parkService;

    public FindAllParks(View view, ParkService parkService) {
        this.view = view;
        this.parkService = parkService;
    }

    @Override
    public String name() {
        return "find-all-parks";
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
        return "Lista todos los parques";
    }

    @Override
    public void execute(CommandParams params) {
        List<Park> parks = this.parkService.findAll();
        this.view.showList("Parks", parks);
    }
}
