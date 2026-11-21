package es.upm.poo.configuration;

import es.upm.poo.data.model.User;
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
    void testSeedCreatesFiveItemsPerModel() {
        Context context = this.createContext();

        context.seeder().seed();

        assertEquals(5, context.userService().findAll().size());
        assertEquals(5, context.parkService().findAll().size());
        assertEquals(5, context.zoneService().findAll().size());
        assertEquals(5, context.attractionService().findAll().size());
        assertEquals(5, context.consentRepository().findAll().size());
        assertEquals(5, context.ticketService().findAll().size());
    }
}
