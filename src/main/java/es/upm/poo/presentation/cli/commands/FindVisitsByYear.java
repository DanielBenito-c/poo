package es.upm.poo.presentation.cli.commands;

import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.exceptions.BadRequestException;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.TicketService;

import java.util.List;

public class FindVisitsByYear implements Command {
    private static final int MIN_YEAR = 1;

    private final View view;
    private final TicketService ticketService;

    public FindVisitsByYear(View view, TicketService ticketService) {
        this.view = view;
        this.ticketService = ticketService;
    }

    @Override
    public String name() {
        return "find-visits-by-year";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("year");
    }

    @Override
    public List<String> optionalParams() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Devuelve por email el número de visitas de cada usuario en el año indicado";
    }

    @Override
    public void execute(CommandParams params) {
        Integer year = params.getInteger(0);
        if (year == null || year < MIN_YEAR) {
            throw new BadRequestException("year no es válido: " + year);
        }
        this.view.showList("Visitas por año", this.ticketService.countVisitsByYear(year));
    }
}
