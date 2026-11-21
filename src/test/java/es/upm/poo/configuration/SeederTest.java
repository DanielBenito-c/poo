package es.upm.poo.configuration;

import es.upm.poo.data.model.Attraction;
import es.upm.poo.data.model.Park;
import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.model.User;
import es.upm.poo.data.model.Zone;
import es.upm.poo.data.repositories.ConsentRepository;
import es.upm.poo.data.repositories.map.AttractionRepositoryMap;
import es.upm.poo.data.repositories.map.ConsentRepositoryMap;
import es.upm.poo.data.repositories.map.ParkRepositoryMap;
import es.upm.poo.data.repositories.map.TicketRepositoryMap;
import es.upm.poo.data.repositories.map.UserRepositoryMap;
import es.upm.poo.data.repositories.map.ZoneRepositoryMap;
import es.upm.poo.services.AttractionService;
import es.upm.poo.services.ConsentService;
import es.upm.poo.services.ParkService;
import es.upm.poo.services.TicketService;
import es.upm.poo.services.UserService;
import es.upm.poo.services.ZoneService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeederTest {

    private record Context(UserService userService, ConsentRepository consentRepository,
                           ParkService parkService, ZoneService zoneService,
                           AttractionService attractionService, TicketService ticketService,
                           Seeder seeder) {
    }

    private Context createContext() {
        UserRepositoryMap userRepository = new UserRepositoryMap();
        ConsentRepositoryMap consentRepository = new ConsentRepositoryMap();
        ParkRepositoryMap parkRepository = new ParkRepositoryMap();
        ZoneRepositoryMap zoneRepository = new ZoneRepositoryMap();
        AttractionRepositoryMap attractionRepository = new AttractionRepositoryMap();
        TicketRepositoryMap ticketRepository = new TicketRepositoryMap();

        UserService userService = new UserService(userRepository);
        ConsentService consentService = new ConsentService(userRepository, consentRepository);
        ParkService parkService = new ParkService(parkRepository);
        ZoneService zoneService = new ZoneService(zoneRepository, parkRepository);
        AttractionService attractionService = new AttractionService(attractionRepository, zoneRepository);
        TicketService ticketService = new TicketService(ticketRepository, userRepository, parkRepository);
        Seeder seeder = new Seeder(userService, consentService, parkService, zoneService,
                attractionService, ticketService);
        return new Context(userService, consentRepository, parkService, zoneService,
                attractionService, ticketService, seeder);
    }

    @Test
    void testSeedCreatesExpectedUsers() {
        Context context = this.createContext();

        context.seeder().seed();

        assertEquals(5, context.userService().findAll().size());
        assertTrue(context.userService().findUserByEmail("alice.johnson@upm.es").isActive());
        assertTrue(context.userService().findUserByEmail("marco.rossi@upm.es").isActive());
        assertTrue(context.userService().findUserByEmail("nadia.chen@upm.es").isActive());
        assertTrue(context.userService().findUserByEmail("omar.haddad@upm.es").isActive());
        assertTrue(context.userService().findUserByEmail("sofia.garcia@upm.es").isActive());
    }

    @Test
    void testSeedKeepsSeededUserData() {
        Context context = this.createContext();

        context.seeder().seed();

        User alice = context.userService().findUserByEmail("alice.johnson@upm.es");
        assertEquals("Alice Johnson", alice.getName());
        assertSame(User.Gender.FEMALE, alice.getGender());
        assertEquals(1L, alice.getId());
    }

    @Test
    void testSeedCreatesAtLeastFiveItemsPerModel() {
        Context context = this.createContext();

        context.seeder().seed();

        assertTrue(context.userService().findAll().size() >= 5);
        assertTrue(context.parkService().findAll().size() >= 5);
        assertTrue(context.zoneService().findAll().size() >= 5);
        assertTrue(context.attractionService().findAll().size() >= 5);
        assertTrue(context.consentRepository().findAll().size() >= 5);
        assertTrue(context.ticketService().findAll().size() >= 5);
    }

    @Test
    void testSeedCreatesParkWithSeveralZones() {
        Context context = this.createContext();

        context.seeder().seed();

        Park magicKingdom = context.parkService().findAll().stream()
                .filter(park -> "Magic Kingdom".equals(park.getName()))
                .findFirst().orElseThrow();
        assertEquals(2, magicKingdom.getZones().size());
    }

    @Test
    void testSeedCreatesZoneWithSeveralAttractions() {
        Context context = this.createContext();

        context.seeder().seed();

        Zone tomorrowland = context.zoneService().findAll().stream()
                .filter(zone -> "Tomorrowland".equals(zone.getName()))
                .findFirst().orElseThrow();
        assertEquals(2, tomorrowland.getAttractions().size());
    }

    @Test
    void testSeedRegistersEveryParkZoneInZoneRepository() {
        Context context = this.createContext();

        context.seeder().seed();

        for (Park park : context.parkService().findAll()) {
            for (Zone zone : park.getZones()) {
                assertNotNull(zone.getId());
                assertSame(zone, context.zoneService().readZone(zone.getId()));
            }
        }
    }

    @Test
    void testSeedAssociatesEveryPersistedAttractionWithAZone() {
        Context context = this.createContext();

        context.seeder().seed();

        var zonedAttractions = context.zoneService().findAll().stream()
                .flatMap(zone -> zone.getAttractions().stream())
                .toList();
        assertEquals(context.attractionService().findAll().size(), zonedAttractions.size());
        for (Attraction attraction : zonedAttractions) {
            assertNotNull(attraction.getId());
            assertTrue(context.attractionService().findAll().contains(attraction));
        }
    }

    @Test
    void testSeedCreatesTicketsWithPersistedUserAndPark() {
        Context context = this.createContext();

        context.seeder().seed();

        for (Ticket ticket : context.ticketService().findAll()) {
            assertNotNull(ticket.getId());
            assertNotNull(ticket.getUser());
            assertNotNull(ticket.getPark());
        }
    }
}
