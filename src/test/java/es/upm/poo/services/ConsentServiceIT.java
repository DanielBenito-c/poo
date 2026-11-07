package es.upm.poo.services;

import es.upm.poo.data.model.Consent;
import es.upm.poo.data.model.User;
import es.upm.poo.data.repositories.ConsentRepository;
import es.upm.poo.data.repositories.UserRepository;
import es.upm.poo.data.repositories.map.ConsentRepositoryMap;
import es.upm.poo.data.repositories.map.UserRepositoryMap;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ConsentServiceIT {

    private static final String SOURCE = "web";

    private User createUser(long id) {
        User user = new User("Daniel", "daniel@spotify.com", LocalDate.of(1990, 1, 1),
                User.Gender.MALE, true);
        user.setId(id);
        return user;
    }

    @Test
    void testCreatePersistsConsentAndPreservesAssociatedUser() {
        UserRepository userRepository = new UserRepositoryMap();
        ConsentRepository consentRepository = new ConsentRepositoryMap();
        ConsentService service = new ConsentService(userRepository, consentRepository);
        User user = this.createUser(17L);
        Consent consent = new Consent(user, SOURCE, Consent.ConsentPurpose.MARKETING);

        Consent createdConsent = service.create(consent);

        assertSame(consent, createdConsent);
        assertEquals(1L, createdConsent.getId());
        assertSame(user, createdConsent.getUser());
        assertSame(consent, consentRepository.read(1L).orElseThrow());
    }

    @Test
    void testCreateAssignsSequentialIds() {
        ConsentService service = new ConsentService(new UserRepositoryMap(), new ConsentRepositoryMap());

        Consent first = service.create(new Consent(this.createUser(17L), SOURCE, Consent.ConsentPurpose.BASIC));
        Consent second = service.create(new Consent(this.createUser(18L), SOURCE, Consent.ConsentPurpose.ALL));

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
    }
}
