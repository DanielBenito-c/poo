package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Ticket;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.TicketService;

import java.util.List;

public class FindAllTickets implements Command {
    private final View view;
    private final TicketService ticketService;

    public FindAllTickets(View view, TicketService ticketService) {
        this.view = view;
        this.ticketService = ticketService;
    }

    @Override
    public String name() {
        return "find-all-tickets";
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
        return "Lista todos los tickets";
    }

    @Override
    public void execute(CommandParams params) {
        List<Ticket> tickets = this.ticketService.findAll();
        this.view.showList("Tickets", tickets);
    }
}
