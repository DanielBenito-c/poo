package es.upm.poo.configuration;

import es.upm.poo.data.model.Attraction;
import es.upm.poo.data.model.Consent;
import es.upm.poo.data.model.Park;
import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.model.User;
import es.upm.poo.data.model.Zone;
import es.upm.poo.services.AttractionService;
import es.upm.poo.services.ConsentService;
import es.upm.poo.services.ParkService;
import es.upm.poo.services.TicketService;
import es.upm.poo.services.UserService;
import es.upm.poo.services.ZoneService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Seeder {
    private final UserService userService;
    private final ConsentService consentService;
    private final ParkService parkService;
    private final ZoneService zoneService;
    private final AttractionService attractionService;
    private final TicketService ticketService;

    public Seeder(UserService userService, ConsentService consentService, ParkService parkService,
                  ZoneService zoneService, AttractionService attractionService, TicketService ticketService) {
        this.userService = userService;
        this.consentService = consentService;
        this.parkService = parkService;
        this.zoneService = zoneService;
        this.attractionService = attractionService;
        this.ticketService = ticketService;
    }

    public void seed() {
        List<User> users = this.createUsers();
        List<Park> parks = this.createParks();
        this.createAttractions(this.collectZones(parks));
        this.createConsents(users);
        this.createTickets(users, parks);
    }

    private List<User> createUsers() {
        return List.of(
                this.userService.create(new User("Alice Johnson", "alice.johnson@upm.es",
                        LocalDate.of(2007, 5, 15), User.Gender.FEMALE, null)),
                this.userService.create(new User("Marco Rossi", "marco.rossi@upm.es",
                        LocalDate.of(2007, 1, 1), User.Gender.MALE, true)),
                this.userService.create(new User("Nadia Chen", "nadia.chen@upm.es",
                        LocalDate.of(2000, 1, 1), User.Gender.FEMALE, true)),
                this.userService.create(new User("Omar Haddad", "omar.haddad@upm.es",
                        LocalDate.of(1965, 8, 20), User.Gender.OTHER, true)),
                this.userService.create(new User("Sofia Garcia", "sofia.garcia@upm.es",
                        LocalDate.of(1992, 11, 10), User.Gender.FEMALE, true)));
    }

    private List<Park> createParks() {
        return List.of(
                this.registerPark(new Park("Magic Kingdom", "Orlando, Florida",
                        LocalTime.of(9, 0), LocalTime.of(22, 0), "Fantasyland", "Fantasy")),
                this.registerPark(new Park("Epcot", "Orlando, Florida",
                        LocalTime.of(9, 0), LocalTime.of(21, 0), "World Showcase", "Cultures")),
                this.registerPark(new Park("PortAventura", "Salou, Tarragona",
                        LocalTime.of(10, 0), LocalTime.of(20, 0), "Mediterrania", "Adventure")),
                this.registerPark(new Park("Disneyland Paris", "Marne-la-Vallee, France",
                        LocalTime.of(9, 30), LocalTime.of(23, 0), "Adventureland", "Pirates")),
                this.registerPark(new Park("Warner Bros. Madrid", "San Martin de la Vega, Madrid",
                        LocalTime.of(11, 0), LocalTime.of(20, 0), "DC Super Heroes", "Superheroes")));
    }

    private Park registerPark(Park park) {
        this.parkService.create(park);
        for (Zone zone : park.getZones()) {
            this.zoneService.create(zone);
        }
        return park;
    }

    private List<Zone> collectZones(List<Park> parks) {
        return parks.stream().flatMap(park -> park.getZones().stream()).toList();
    }

    private void createAttractions(List<Zone> zones) {
        List<Attraction> attractions = List.of(
                this.attractionService.create(new Attraction("Space Mountain", 120, 200)),
                this.attractionService.create(new Attraction("Frozen Ever After", null, 150)),
                this.attractionService.create(new Attraction("Shambhala", 140, 100)),
                this.attractionService.create(new Attraction("Pirates of the Caribbean", null, 180)),
                this.attractionService.create(new Attraction("Batman: Arkham Asylum", 130, 90)));
        for (int index = 0; index < attractions.size(); index++) {
            zones.get(index).addAttraction(attractions.get(index));
        }
    }

    private void createConsents(List<User> users) {
        this.consentService.create(new Consent(users.get(0), "web", Consent.ConsentPurpose.BASIC));
        this.consentService.create(new Consent(users.get(1), "mobile-app", Consent.ConsentPurpose.MARKETING));
        this.consentService.create(new Consent(users.get(2), "web", Consent.ConsentPurpose.ALL));
        this.consentService.create(new Consent(users.get(3), "kiosk", Consent.ConsentPurpose.BASIC));
        this.consentService.create(new Consent(users.get(4), "mobile-app", Consent.ConsentPurpose.MARKETING));
    }

    private void createTickets(List<User> users, List<Park> parks) {
        this.createTicket(users.get(0), parks.get(0), new Ticket(Ticket.TicketType.STANDARD,
                new BigDecimal("55.00"), LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 5)));
        this.createTicket(users.get(1), parks.get(1), new Ticket(Ticket.TicketType.FAST_PASS,
                new BigDecimal("120.00"), LocalDate.of(2026, 3, 2), LocalDate.of(2026, 3, 6)));
        this.createTicket(users.get(2), parks.get(2), new Ticket(Ticket.TicketType.STANDARD,
                new BigDecimal("45.00"), LocalDate.of(2026, 4, 10), LocalDate.of(2026, 4, 12)));
        this.createTicket(users.get(3), parks.get(3), new Ticket(Ticket.TicketType.FAST_PASS,
                new BigDecimal("130.00"), LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 3)));
        this.createTicket(users.get(4), parks.get(4), new Ticket(Ticket.TicketType.STANDARD,
                new BigDecimal("39.00"), LocalDate.of(2026, 6, 15), LocalDate.of(2026, 6, 20)));
    }

    private Ticket createTicket(User user, Park park, Ticket ticket) {
        ticket.setUser(user);
        ticket.setPark(park);
        return this.ticketService.create(ticket);
    }
}
