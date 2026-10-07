package es.upm.poo;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private static final String NAME = "Daniel";
    private static final String EMAIL = "daniel@spotify.com";

    @Test
    void testConstructorStoresValidValues() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        assertEquals(NAME, user.getName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(dateOfBirth, user.getDateOfBirth());
        assertEquals(User.Gender.MALE, user.getGender());
        assertTrue(user.isActive());
    }

    @Test
    void testConstructorWithActiveNullDefaultsToTrue() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, null);

        assertTrue(user.isActive());
    }

    @Test
    void testConstructorWithInvalidNameThrowsException() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);

        assertThrows(IllegalArgumentException.class,
                () -> new User(null, EMAIL, dateOfBirth, User.Gender.MALE, true));
        assertThrows(IllegalArgumentException.class,
                () -> new User("", EMAIL, dateOfBirth, User.Gender.MALE, true));
        assertThrows(IllegalArgumentException.class,
                () -> new User("   ", EMAIL, dateOfBirth, User.Gender.MALE, true));
    }

    @Test
    void testSetNameStoresValue() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        user.setName("Ana");

        assertEquals("Ana", user.getName());
    }

    @Test
    void testSetEmailWithInvalidValueThrowsException() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        assertThrows(IllegalArgumentException.class, () -> user.setEmail(null));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("spotify.com"));
    }

    @Test
    void testSetEmailStoresValidValue() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        user.setEmail("nuevo@spotify.com");

        assertEquals("nuevo@spotify.com", user.getEmail());
    }

    @Test
    void testSetDateOfBirthWithNullThrowsExceptionInsteadOfNullPointerException() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        assertThrows(IllegalArgumentException.class, () -> user.setDateOfBirth(null));
    }

    @Test
    void testSetDateOfBirthUnderMinAgeThrowsException() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        LocalDate underage = LocalDate.now().minusYears(12);
        assertThrows(IllegalArgumentException.class, () -> user.setDateOfBirth(underage));
    }

    @Test
    void testSetDateOfBirthAtMinAgeStoresValue() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        LocalDate thirteenYearsOld = LocalDate.now().minusYears(13);
        user.setDateOfBirth(thirteenYearsOld);

        assertEquals(thirteenYearsOld, user.getDateOfBirth());
    }

    @Test
    void testSetDateOfBirthInFutureThrowsException() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        LocalDate futureDate = LocalDate.now().plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> user.setDateOfBirth(futureDate));
    }

    @Test
    void testSetGenderWithNullThrowsException() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        assertThrows(IllegalArgumentException.class, () -> user.setGender(null));
    }

    @Test
    void testSetGenderStoresValue() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        user.setGender(User.Gender.NON_BINARY);

        assertEquals(User.Gender.NON_BINARY, user.getGender());
    }

    @Test
    void testSetActiveWithFalseStoresValue() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        user.setActive(false);

        assertFalse(user.isActive());
    }

    @Test
    void testSetActiveWithNullRestoresDefaultValue() {
        LocalDate dateOfBirth = LocalDate.now().minusYears(30);
        User user = new User(NAME, EMAIL, dateOfBirth, User.Gender.MALE, true);

        user.setActive(false);
        user.setActive(null);

        assertTrue(user.isActive());
    }
}
