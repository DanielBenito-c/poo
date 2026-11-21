package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Park;
import es.upm.poo.data.model.Zone;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.ParkService;
import es.upm.poo.services.ZoneService;

import java.util.List;

public class CreatePark implements Command {
    private final View view;
    private final ParkService parkService;
    private final ZoneService zoneService;

    public CreatePark(View view, ParkService parkService, ZoneService zoneService) {
        this.view = view;
        this.parkService = parkService;
        this.zoneService = zoneService;
    }

    @Override
    public String name() {
        return "create-park";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("name", "address", "openingTime", "closingTime", "zoneName", "zoneTheme");
    }

    @Override
    public List<String> optionalParams() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Se crea un parque";
    }

    @Override
    public void execute(CommandParams params) {
        Park newPark = new Park(params.getString(0), params.getString(1),
                params.getLocalTime(2), params.getLocalTime(3),
                params.getString(4), params.getString(5));
        Park createdPark = this.parkService.create(newPark);
        this.registerZones(createdPark);
        this.view.showItem(createdPark);
    }

    private void registerZones(Park park) {
        for (Zone zone : park.getZones()) {
            this.zoneService.create(zone);
        }
    }
}
