package es.upm.poo.services;

import java.time.LocalDate;

import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.map.UserRepositoryMap;

import es.upm.poo.services.exceptions.ConflictException;
import es.upm.poo.services.exceptions.NotFoundException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
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
        assertEquals(1, service.findAll().size());
    }

    @Test
    void testCreateWithEmailDifferingOnlyByCaseThrowsConflictException() {
        UserService service = this.createUserService();
        service.create(this.createUser(EMAIL));

        ConflictException exception = assertThrows(ConflictException.class,
                () -> service.create(this.createUser("DANIEL@SPOTIFY.COM")));

        assertTrue(exception.getMessage().contains("DANIEL@SPOTIFY.COM"));
        assertEquals(1, service.findAll().size());
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

    @Test
    void testFindUserByEmailReturnsMatchingUser() {
        UserService service = this.createUserService();
        User expectedUser = service.create(this.createUser(EMAIL));

        User foundUser = service.findUserByEmail(EMAIL);

        assertSame(expectedUser, foundUser);
    }

    @Test
    void testFindUserByUnknownEmailThrowsNotFoundExceptionWithEmail() {
        UserService service = this.createUserService();

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.findUserByEmail(EMAIL));

        assertTrue(exception.getMessage().contains(EMAIL));
    }

    @Test
    void testFindUserByEmailIgnoresCase() {
        UserService service = this.createUserService();
        User expectedUser = service.create(this.createUser(EMAIL));

        User foundUser = service.findUserByEmail("DANIEL@SPOTIFY.COM");

        assertSame(expectedUser, foundUser);
    }

    @Test
    void testReadUserReturnsUserWithRequestedId() {
        UserService service = this.createUserService();
        User expectedUser = service.create(this.createUser(EMAIL));

        User foundUser = service.readUser(expectedUser.getId());

        assertSame(expectedUser, foundUser);
    }

    @Test
    void testReadUnknownUserThrowsNotFoundExceptionWithId() {
        UserService service = this.createUserService();

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.readUser(42L));

        assertTrue(exception.getMessage().contains("42"));
    }

    @Test
    void testDeleteUserRemovesUser() {
        UserService service = this.createUserService();
        User createdUser = service.create(this.createUser(EMAIL));

        service.deleteUser(createdUser.getId());

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void testDeleteUnknownUserDoesNothing() {
        UserService service = this.createUserService();
        service.create(this.createUser(EMAIL));

        service.deleteUser(42L);

        assertFalse(service.findAll().isEmpty());
    }
}
