package es.upm.poo.data.repositories;

import es.upm.poo.data.model.Consent;
import es.upm.poo.data.repositories.map.ConsentRepositoryMap;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ConsentRepositoryIT {

    @Test
    void testCreateAssignsConsentIdWithoutReplacingUserId() {
        ConsentRepository repository = new ConsentRepositoryMap();
        Consent consent = new Consent(17L, "web", Consent.ConsentPurpose.BASIC,
                LocalDateTime.of(2026, 10, 1, 12, 30));

        Consent createdConsent = repository.create(consent);

        assertSame(consent, createdConsent);
        assertEquals(1L, createdConsent.getId());
        assertEquals(17L, createdConsent.getUserId());
        assertSame(consent, repository.read(1L).orElseThrow());
    }
}
