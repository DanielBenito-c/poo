package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Attraction;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.AttractionService;

import java.util.List;

public class FindAllAttractions implements Command {
    private final View view;
    private final AttractionService attractionService;

    public FindAllAttractions(View view, AttractionService attractionService) {
        this.view = view;
        this.attractionService = attractionService;
    }

    @Override
    public String name() {
        return "find-all-attractions";
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
        return "Lista todas las atracciones";
    }

    @Override
    public void execute(CommandParams params) {
        List<Attraction> attractions = this.attractionService.findAll();
        this.view.showList("Attractions", attractions);
    }
}
