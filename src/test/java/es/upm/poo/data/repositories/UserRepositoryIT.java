package es.upm.poo.data.repositories;

import java.time.LocalDate;

import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.map.UserRepositoryMap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryIT {

    private static final String NAME = "Daniel";
    private static final String EMAIL = "daniel@spotify.com";

    private UserRepository createUserRepository() {
        return new UserRepositoryMap();
    }

    private User createUser(String email) {
        return new User(NAME, email, LocalDate.now().minusYears(30), User.Gender.MALE, true);
    }

    @Test
    void testCreateAssignsUniqueSequentialIdsStartingAtOne() {
        UserRepository repository = this.createUserRepository();

        User first = this.createUser(EMAIL);
        User second = this.createUser("ana@spotify.com");

        assertSame(first, repository.create(first));
        repository.create(second);

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
    }

    @Test
    void testReadExistingIdReturnsUser() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser(EMAIL));

        User user = repository.read(1L).orElseThrow();

        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    void testReadUnknownIdReturnsEmpty() {
        UserRepository repository = this.createUserRepository();

        assertTrue(repository.read(99L).isEmpty());
    }

    @Test
    void testUpdateExistingIdReplacesUserKeepingId() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser(EMAIL));
        User updated = this.createUser("nueva@spotify.com");

        User result = repository.update(1L, updated);

        assertEquals(1L, result.getId());
        assertEquals("nueva@spotify.com", repository.read(1L).orElseThrow().getEmail());
    }

    @Test
    void testUpdateUnknownIdStoresUserWithGivenId() {
        UserRepository repository = this.createUserRepository();

        User result = repository.update(42L, this.createUser(EMAIL));

        assertEquals(42L, result.getId());
        assertEquals(EMAIL, repository.read(42L).orElseThrow().getEmail());
    }

    @Test
    void testDeleteByIdRemovesUser() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser(EMAIL));

        repository.deleteById(1L);

        assertTrue(repository.read(1L).isEmpty());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void testDeleteByUnknownIdDoesNothing() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser(EMAIL));

        repository.deleteById(99L);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void testFindAllReturnsEmptyListInitially() {
        UserRepository repository = this.createUserRepository();

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void testFindAllReturnsAllStoredUsers() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser(EMAIL));
        repository.create(this.createUser("ana@spotify.com"));

        assertEquals(2, repository.findAll().size());
    }

    @Test
    void testFindByEmailReturnsStoredUser() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser(EMAIL));

        User user = repository.findByEmail(EMAIL).orElseThrow();

        assertEquals(NAME, user.getName());
    }

    @Test
    void testFindByUnknownEmailReturnsEmpty() {
        UserRepository repository = this.createUserRepository();

        assertTrue(repository.findByEmail("ana@spotify.com").isEmpty());
    }

    @Test
    void testFindByEmailWithNullReturnsEmpty() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser(EMAIL));

        assertTrue(repository.findByEmail(null).isEmpty());
    }
}
