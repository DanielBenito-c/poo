package es.upm.poo.presentation.cli.commands;

import es.upm.poo.data.model.Park;
import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.model.User;
import es.upm.poo.presentation.cli.Command;
import es.upm.poo.presentation.cli.CommandParams;
import es.upm.poo.presentation.view.View;
import es.upm.poo.services.ParkService;
import es.upm.poo.services.TicketService;
import es.upm.poo.services.UserService;

import java.util.List;

public class CreateTicket implements Command {
    private final View view;
    private final TicketService ticketService;
    private final UserService userService;
    private final ParkService parkService;

    public CreateTicket(View view, TicketService ticketService, UserService userService,
                        ParkService parkService) {
        this.view = view;
        this.ticketService = ticketService;
        this.userService = userService;
        this.parkService = parkService;
    }

    @Override
    public String name() {
        return "create-ticket";
    }

    @Override
    public List<String> obligatoryParams() {
        return List.of("userId", "parkId", "ticketType", "price", "purchaseDate", "validDate");
    }

    @Override
    public List<String> optionalParams() {
        return List.of();
    }

    @Override
    public String helpMessage() {
        return "Se crea un ticket";
    }

    @Override
    public void execute(CommandParams params) {
        User user = this.userService.readUser(params.getLong(0));
        Park park = this.parkService.readPark(params.getLong(1));
        Ticket newTicket = new Ticket(params.getTicketType(2), params.getBigDecimal(3),
                params.getLocalDate(4), params.getLocalDate(5));
        newTicket.setUser(user);
        newTicket.setPark(park);
        Ticket createdTicket = this.ticketService.create(newTicket);
        this.view.showItem(createdTicket);
    }
}
