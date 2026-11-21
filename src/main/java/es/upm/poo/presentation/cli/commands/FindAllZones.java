package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Zone;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.ZoneService;

import java.util.List;

public class FindAllZones implements Command {
    private final View view;
    private final ZoneService zoneService;

    public FindAllZones(View view, ZoneService zoneService) {
        this.view = view;
        this.zoneService = zoneService;
    }

    @Override
    public String name() {
        return "find-all-zones";
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
        return "Lista todas las zonas";
    }

    @Override
    public void execute(CommandParams params) {
        List<Zone> zones = this.zoneService.findAll();
        this.view.showList("Zones", zones);
    }
}
