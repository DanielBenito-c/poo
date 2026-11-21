package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Attraction;
import es.upm.poo.data.model.Zone;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.AttractionService;
import es.upm.poo.services.ZoneService;

import java.util.List;

public class CreateAttraction implements Command {
    private final View view;
    private final AttractionService attractionService;
    private final ZoneService zoneService;

    public CreateAttraction(View view, AttractionService attractionService, ZoneService zoneService) {
        this.view = view;
        this.attractionService = attractionService;
        this.zoneService = zoneService;
    }

    @Override
    public String name() {
        return "create-attraction";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("zoneId", "name", "capacity");
    }

    @Override
    public List<String> optionalParams() {
        return List.of("minHeightCm");
    }

    @Override
    public String helpMessage() {
        return "Se crea una atracción en una zona";
    }

    @Override
    public void execute(CommandParams params) {
        Zone zone = this.zoneService.readZone(params.getLong(0));
        Attraction newAttraction = new Attraction(params.getString(1),
                params.getInteger(3), params.getInteger(2));
        Attraction createdAttraction = this.attractionService.create(newAttraction);
        zone.addAttraction(createdAttraction);
        this.view.showItem(createdAttraction);
    }
}
