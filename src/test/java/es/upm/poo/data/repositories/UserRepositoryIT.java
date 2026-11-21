package es.upm.poo.data.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
        return this.createUser(NAME, email);
    }

    private User createUser(String name, String email) {
        return new User(name, email, LocalDate.now().minusYears(30), User.Gender.MALE, true);
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
    void testFindByEmailIgnoresCase() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser(EMAIL));

        User user = repository.findByEmail("DANIEL@SPOTIFY.COM").orElseThrow();

        assertEquals(EMAIL, user.getEmail());
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

    @Test
    void testFindByNameContainingReturnsMatchingUsers() {
        UserRepository repository = this.createUserRepository();
        User daniel = repository.create(this.createUser("Daniel", EMAIL));
        repository.create(this.createUser("Laura", "laura@spotify.com"));

        assertEquals(List.of(daniel), repository.findByNameContaining("Dan"));
    }

    @Test
    void testFindByNameContainingReturnsAllUsersWithMatchingText() {
        UserRepository repository = this.createUserRepository();
        User alice = repository.create(this.createUser("Alice Johnson", "alice@spotify.com"));
        User alicia = repository.create(this.createUser("Alicia Keys", "alicia@spotify.com"));
        repository.create(this.createUser("Marco Rossi", "marco@spotify.com"));

        assertEquals(Set.of(alice, alicia), Set.copyOf(repository.findByNameContaining("ali")));
    }

    @Test
    void testFindByNameContainingIgnoresCase() {
        UserRepository repository = this.createUserRepository();
        User daniel = repository.create(this.createUser("Daniel", EMAIL));

        assertEquals(List.of(daniel), repository.findByNameContaining("DANIEL"));
    }

    @Test
    void testFindByNameContainingWithUnknownNameReturnsEmpty() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser("Daniel", EMAIL));

        assertTrue(repository.findByNameContaining("Laura").isEmpty());
    }

    @Test
    void testFindByNameContainingWithNullReturnsEmpty() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser("Daniel", EMAIL));

        assertTrue(repository.findByNameContaining(null).isEmpty());
    }

    @Test
    void testFindByNameContainingWithBlankReturnsEmpty() {
        UserRepository repository = this.createUserRepository();
        repository.create(this.createUser("Daniel", EMAIL));

        assertTrue(repository.findByNameContaining("   ").isEmpty());
    }
}
