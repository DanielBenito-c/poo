package es.upm.poo.configuration;

import es.upm.poo.data.repositories.ConsentRepository;
import es.upm.poo.data.repositories.ParkRepository;
import es.upm.poo.data.repositories.TicketRepository;
import es.upm.poo.data.repositories.map.ConsentRepositoryMap;
import es.upm.poo.data.repositories.map.ParkRepositoryMap;
import es.upm.poo.data.repositories.map.TicketRepositoryMap;
import es.upm.poo.presentation.cli.CommandLineInterface;
import es.upm.poo.presentation.ErrorHandler;
import es.upm.poo.presentation.cli.commands.*;
import es.upm.poo.presentation.view.View;
import es.upm.poo.data.repositories.UserRepository;
import es.upm.poo.data.repositories.map.UserRepositoryMap;
import es.upm.poo.services.ParkService;
import es.upm.poo.services.TicketService;
import es.upm.poo.services.UserService;
import es.upm.poo.services.ConsentService;

public class DependencyInjector {
    private static final DependencyInjector instance = new DependencyInjector();

    private final ErrorHandler errorHandler;
    private final View view;
    private final CommandLineInterface commandLineInterface;

    private final UserService userService;
    private final UserRepository userRepository;
    private final ConsentService consentService;
    private final ConsentRepository consentRepository;
    private final ParkService parkService;
    private final ParkRepository parkRepository;
    private final TicketService ticketService;
    private final TicketRepository ticketRepository;

    private DependencyInjector() {
        this.userRepository = new UserRepositoryMap();
        this.userService = new UserService(this.userRepository);
        this.consentRepository = new ConsentRepositoryMap();
        this.consentService = new ConsentService(this.userRepository, this.consentRepository);
        this.parkRepository = new ParkRepositoryMap();
        this.parkService = new ParkService(this.parkRepository);
        this.ticketRepository = new TicketRepositoryMap();
        this.ticketService = new TicketService(this.ticketRepository, this.userRepository, this.parkRepository);
        new Seeder(this.userService).seed();
        this.view = new View();
        this.commandLineInterface = new CommandLineInterface(this.view);
        this.commandLineInterface.add(new Help(this.commandLineInterface));
        this.commandLineInterface.add(new Exit());

        this.commandLineInterface.add(new CreateUser(this.view, this.userService));
        this.commandLineInterface.add(new FindAllUsers(this.view,this.userService));
        this.commandLineInterface.add(new ReadUser(this.view,this.userService));
        this.commandLineInterface.add(new FindUserByEmail(this.view, this.userService));
        this.commandLineInterface.add(new DeleteUser(this.view,this.userService));
        this.commandLineInterface.add(new CreateConsent(this.view,this.consentService,this.userService));
        this.commandLineInterface.add(new CreateTicket(this.view, this.ticketService,
                this.userService, this.parkService));
        this.commandLineInterface.add(new FindAllTickets(this.view, this.ticketService));

        this.errorHandler = new ErrorHandler();
    }

    public static DependencyInjector getInstance() {
        return DependencyInjector.instance;
    }

    public void run() {
        this.errorHandler.handlesErrors(this.commandLineInterface, this.view);
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public View getView() {
        return view;
    }

    public CommandLineInterface getCommandLineInterface() {
        return commandLineInterface;
    }

    public UserService getUserService() {
        return userService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

}
