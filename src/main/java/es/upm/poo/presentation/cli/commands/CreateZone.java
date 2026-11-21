package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Park;
import es.upm.poo.data.model.Zone;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.ParkService;
import es.upm.poo.services.ZoneService;

import java.util.List;

public class CreateZone implements Command {
    private final View view;
    private final ZoneService zoneService;
    private final ParkService parkService;

    public CreateZone(View view, ZoneService zoneService, ParkService parkService) {
        this.view = view;
        this.zoneService = zoneService;
        this.parkService = parkService;
    }

    @Override
    public String name() {
        return "create-zone";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("parkId", "name", "theme");
    }

    @Override
    public List<String> optionalParams() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Se crea una zona en un parque";
    }

    @Override
    public void execute(CommandParams params) {
        Park park = this.parkService.readPark(params.getLong(0));
        Zone createdZone = park.addZone(params.getString(1), params.getString(2));
        this.zoneService.create(createdZone);
        this.view.showItem(createdZone);
    }
}
