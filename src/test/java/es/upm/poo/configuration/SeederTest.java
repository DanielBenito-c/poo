package es.upm.poo.configuration;

import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.map.UserRepositoryMap;
import es.upm.poo.services.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeederTest {

    @Test
    void testSeedCreatesExpectedUsers() {
        UserService userService = new UserService(new UserRepositoryMap());

        new Seeder(userService).seed();

        assertEquals(5, userService.findAll().size());
        assertTrue(userService.findUserByEmail("alice.johnson@upm.es").isActive());
        assertTrue(userService.findUserByEmail("marco.rossi@upm.es").isActive());
        assertTrue(userService.findUserByEmail("nadia.chen@upm.es").isActive());
        assertTrue(userService.findUserByEmail("omar.haddad@upm.es").isActive());
        assertTrue(userService.findUserByEmail("sofia.garcia@upm.es").isActive());
    }

    @Test
    void testSeedKeepsSeededUserData() {
        UserService userService = new UserService(new UserRepositoryMap());

        new Seeder(userService).seed();

        User alice = userService.findUserByEmail("alice.johnson@upm.es");
        assertEquals("Alice Johnson", alice.getName());
        assertSame(User.Gender.FEMALE, alice.getGender());
        assertEquals(1L, alice.getId());
    }
}
