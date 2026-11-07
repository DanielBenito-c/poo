package es.upm.poo.data.repositories;

import es.upm.poo.data.model.Consent;
import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.map.ConsentRepositoryMap;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsentRepositoryIT {

    private static final String SOURCE = "web";

    private ConsentRepository createConsentRepository() {
        return new ConsentRepositoryMap();
    }

    private User createUser(long id) {
        User user = new User("Daniel", "daniel@spotify.com", LocalDate.of(1990, 1, 1),
                User.Gender.MALE, true);
        user.setId(id);
        return user;
    }

    private Consent createConsent(long userId) {
        return new Consent(this.createUser(userId), SOURCE, Consent.ConsentPurpose.BASIC);
    }

    @Test
    void testCreateAssignsConsentIdWithoutReplacingAssociatedUser() {
        ConsentRepository repository = this.createConsentRepository();
        Consent consent = this.createConsent(17L);

        Consent createdConsent = repository.create(consent);

        assertSame(consent, createdConsent);
        assertEquals(1L, createdConsent.getId());
        assertEquals(17L, createdConsent.getUser().getId());
        assertSame(consent, repository.read(1L).orElseThrow());
    }

    @Test
    void testCreateAssignsUniqueSequentialIdsStartingAtOne() {
        ConsentRepository repository = this.createConsentRepository();

        Consent first = repository.create(this.createConsent(17L));
        Consent second = repository.create(this.createConsent(18L));

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
    }

    @Test
    void testReadUnknownIdReturnsEmpty() {
        ConsentRepository repository = this.createConsentRepository();

        assertTrue(repository.read(99L).isEmpty());
    }

    @Test
    void testUpdateExistingIdReplacesConsentKeepingId() {
        ConsentRepository repository = this.createConsentRepository();
        repository.create(this.createConsent(17L));
        Consent updated = new Consent(this.createUser(18L), "mobile-app",
                Consent.ConsentPurpose.MARKETING);

        Consent result = repository.update(1L, updated);

        assertEquals(1L, result.getId());
        assertEquals("mobile-app", repository.read(1L).orElseThrow().getSource());
        assertEquals(18L, repository.read(1L).orElseThrow().getUser().getId());
    }

    @Test
    void testDeleteByIdRemovesConsent() {
        ConsentRepository repository = this.createConsentRepository();
        repository.create(this.createConsent(17L));

        repository.deleteById(1L);

        assertTrue(repository.read(1L).isEmpty());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void testDeleteByUnknownIdDoesNothing() {
        ConsentRepository repository = this.createConsentRepository();
        repository.create(this.createConsent(17L));

        repository.deleteById(99L);

        assertEquals(1, repository.findAll().size());
    }

    @Test
    void testFindAllReturnsEmptyListInitially() {
        ConsentRepository repository = this.createConsentRepository();

        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void testFindAllReturnsAllStoredConsents() {
        ConsentRepository repository = this.createConsentRepository();
        repository.create(this.createConsent(17L));
        repository.create(this.createConsent(18L));

        assertEquals(2, repository.findAll().size());
    }
}
