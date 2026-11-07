package es.upm.poo.data.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsentTest {

    private static final Long USER_ID = 12L;
    private static final String SOURCE = "registration-form";
    private static final Consent.ConsentPurpose PURPOSE = Consent.ConsentPurpose.BASIC;

    private User createUser() {
        User user = new User("Daniel", "daniel@spotify.com", LocalDate.of(1990, 1, 1),
                User.Gender.MALE, true);
        user.setId(USER_ID);
        return user;
    }

    private Consent createConsent() {
        return new Consent(this.createUser(), SOURCE, PURPOSE);
    }

    @Test
    void testConstructorStoresConsentDataAndLeavesPersistenceIdUnset() {
        User user = this.createUser();
        LocalDateTime before = LocalDateTime.now();

        Consent consent = new Consent(user, SOURCE, PURPOSE);

        LocalDateTime after = LocalDateTime.now();
        assertNull(consent.getId());
        assertSame(user, consent.getUser());
        assertEquals(SOURCE, consent.getSource());
        assertEquals(PURPOSE, consent.getPurpose());
        assertFalse(consent.getGrantedAt().isBefore(before));
        assertFalse(consent.getGrantedAt().isAfter(after));
        assertNull(consent.getRevokedAt());
    }

    @Test
    void testConstructorRejectsNullUser() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Consent(null, SOURCE, PURPOSE));

        assertTrue(exception.getMessage().contains("user"));
        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    void testConstructorRejectsNullSource() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Consent(this.createUser(), null, PURPOSE));

        assertTrue(exception.getMessage().contains("source"));
    }

    @Test
    void testConstructorRejectsNullPurpose() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Consent(this.createUser(), SOURCE, null));

        assertTrue(exception.getMessage().contains("purpose"));
    }

    @Test
    void testSetUserReplacesAssociatedUser() {
        Consent consent = this.createConsent();
        User otherUser = this.createUser();
        otherUser.setId(99L);

        consent.setUser(otherUser);

        assertSame(otherUser, consent.getUser());
    }

    @Test
    void testSetUserWithNullThrowsException() {
        Consent consent = this.createConsent();

        assertThrows(IllegalArgumentException.class, () -> consent.setUser(null));
    }

    @Test
    void testSetSourceStoresValue() {
        Consent consent = this.createConsent();

        consent.setSource("mobile-app");

        assertEquals("mobile-app", consent.getSource());
    }

    @Test
    void testSetSourceWithNullThrowsException() {
        Consent consent = this.createConsent();

        assertThrows(IllegalArgumentException.class, () -> consent.setSource(null));
    }

    @Test
    void testSetPurposeStoresValue() {
        Consent consent = this.createConsent();

        consent.setPurpose(Consent.ConsentPurpose.MARKETING);

        assertEquals(Consent.ConsentPurpose.MARKETING, consent.getPurpose());
    }

    @Test
    void testSetPurposeWithNullThrowsException() {
        Consent consent = this.createConsent();

        assertThrows(IllegalArgumentException.class, () -> consent.setPurpose(null));
    }

    @Test
    void testSetIdStoresPersistenceId() {
        Consent consent = this.createConsent();

        consent.setId(3L);

        assertEquals(3L, consent.getId());
    }

    @Test
    void testSetRevokedAtStoresCurrentTimestamp() {
        Consent consent = this.createConsent();
        LocalDateTime before = LocalDateTime.now();

        consent.setRevokedAt();

        LocalDateTime after = LocalDateTime.now();
        assertFalse(consent.getRevokedAt().isBefore(before));
        assertFalse(consent.getRevokedAt().isAfter(after));
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
