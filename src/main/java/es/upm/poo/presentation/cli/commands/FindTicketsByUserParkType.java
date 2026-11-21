package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Ticket;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.TicketService;

import java.util.List;

public class FindTicketsByUserParkType implements Command {
    private final View view;
    private final TicketService ticketService;

    public FindTicketsByUserParkType(View view, TicketService ticketService) {
        this.view = view;
        this.ticketService = ticketService;
    }

    @Override
    public String name() {
        return "find-tickets-by-user-park-type";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("userName", "parkName", "ticketType");
    }

    @Override
    public List<String> optionalParams() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Devuelve los tickets de un usuario y parque con el tipo indicado";
    }

    @Override
    public void execute(CommandParams params) {
        List<Ticket> tickets = this.ticketService.findByUserNameAndParkNameAndTicketType(
                params.getString(0), params.getString(1), params.getTicketType(2));
        this.view.showList("Tickets", tickets);
    }
}
