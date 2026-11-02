package es.upm.poo.data.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsentTest {

    private static final Long USER_ID = 12L;
    private static final String SOURCE = "registration-form";
    private static final Consent.ConsentPurpose PURPOSE = Consent.ConsentPurpose.BASIC;
    private static final LocalDateTime GRANTED_AT = LocalDateTime.of(2026, 10, 1, 12, 30);

    private Consent createConsent() {
        return new Consent(USER_ID, SOURCE, PURPOSE, GRANTED_AT);
    }

    @Test
    void testConstructorStoresConsentDataAndLeavesPersistenceIdUnset() {
        Consent consent = this.createConsent();

        assertNull(consent.getId());
        assertEquals(USER_ID, consent.getUserId());
        assertEquals(SOURCE, consent.getSource());
        assertEquals(PURPOSE, consent.getPurpose());
        assertEquals(GRANTED_AT, consent.getGrantedAt());
        assertNull(consent.getRevokedAt());
    }

    @Test
    void testConstructorRejectsNullUserId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Consent(null, SOURCE, PURPOSE, GRANTED_AT));

        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    void testConstructorRejectsNullSource() {
        assertThrows(IllegalArgumentException.class,
                () -> new Consent(USER_ID, null, PURPOSE, GRANTED_AT));
    }

    @Test
    void testConstructorRejectsNullPurpose() {
        assertThrows(IllegalArgumentException.class,
                () -> new Consent(USER_ID, SOURCE, null, GRANTED_AT));
    }

    @Test
    void testSetIdStoresPersistenceId() {
        Consent consent = this.createConsent();

        consent.setId(3L);

        assertEquals(3L, consent.getId());
    }

    @Test
    void testSetRevokedAtStoresAndClearsTimestamp() {
        Consent consent = this.createConsent();
        LocalDateTime revokedAt = LocalDateTime.of(2026, 10, 2, 9, 0);

        consent.setRevokedAt(revokedAt);

        assertEquals(revokedAt, consent.getRevokedAt());

        consent.setRevokedAt(null);

        assertNull(consent.getRevokedAt());
    }

    @Test
    void testToStringContainsConsentAndUserIds() {
        Consent consent = this.createConsent();
        consent.setId(3L);

        String text = consent.toString();

        assertTrue(text.contains("id=3"));
        assertTrue(text.contains("userId=" + USER_ID));
        assertTrue(text.contains(SOURCE));
        assertTrue(text.contains(PURPOSE.name()));
    }
}
