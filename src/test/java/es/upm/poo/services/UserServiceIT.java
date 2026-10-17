package es.upm.poo.services;

import java.time.LocalDate;

import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.map.UserRepositoryMap;

import es.upm.poo.services.exceptions.ConflictException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceIT {

    private static final String NAME = "Daniel";
    private static final String EMAIL = "daniel@spotify.com";

    private UserService createUserService() {
        return new UserService(new UserRepositoryMap());
    }

    private User createUser(String email) {
        return new User(NAME, email, LocalDate.now().minusYears(30), User.Gender.MALE, true);
    }

    @Test
    void testCreateStoresUserAndAssignsId() {
        UserService service = this.createUserService();

        User created = service.create(this.createUser(EMAIL));

        assertEquals(1L, created.getId());
        assertEquals(1, service.findAll().size());
    }

    @Test
    void testCreateWithDuplicatedEmailThrowsConflictException() {
        UserService service = this.createUserService();
        service.create(this.createUser(EMAIL));

        ConflictException exception = assertThrows(ConflictException.class,
                () -> service.create(this.createUser(EMAIL)));

        assertTrue(exception.getMessage().contains(EMAIL));
    }

    @Test
    void testCreateWithDifferentEmailsStoresBothUsers() {
        UserService service = this.createUserService();
        service.create(this.createUser(EMAIL));

        service.create(this.createUser("ana@spotify.com"));

        assertEquals(2, service.findAll().size());
    }

    @Test
    void testFindAllReturnsEmptyListInitially() {
        UserService service = this.createUserService();

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void testFindAllReturnsCreatedUsers() {
        UserService service = this.createUserService();
        service.create(this.createUser(EMAIL));
        service.create(this.createUser("ana@spotify.com"));

        assertEquals(2, service.findAll().size());
    }
}
