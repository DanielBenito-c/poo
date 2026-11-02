package es.upm.poo.services;

import es.upm.poo.data.model.Consent;
import es.upm.poo.data.repositories.map.ConsentRepositoryMap;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ConsentServiceIT {

    @Test
    void testCreatePersistsConsentAndPreservesAssociatedUserId() {
        ConsentService service = new ConsentService(new ConsentRepositoryMap());
        Consent consent = new Consent(17L, "web", Consent.ConsentPurpose.MARKETING,
                LocalDateTime.of(2026, 10, 1, 12, 30));

        Consent createdConsent = service.create(consent);

        assertSame(consent, createdConsent);
        assertEquals(1L, createdConsent.getId());
        assertEquals(17L, createdConsent.getUserId());
    }
}
