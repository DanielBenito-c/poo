package es.upm.poo.data.model;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private static final String NAME = "Daniel";
    private static final String EMAIL = "daniel@spotify.com";

    private User createUser() {
        return this.createUser(EMAIL);
    }

    private User createUser(String email) {
        return new User(NAME, email, LocalDate.now().minusYears(30), User.Gender.MALE, true);
    }

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
        User user = new User(NAME, EMAIL, LocalDate.now().minusYears(30), User.Gender.MALE, null);

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
        User user = this.createUser();

        user.setName("Ana");

        assertEquals("Ana", user.getName());
    }

    @Test
    void testSetNameAtMaxLengthStoresValue() {
        User user = this.createUser();

        user.setName("a".repeat(50));

        assertEquals("a".repeat(50), user.getName());
    }

    @Test
    void testSetNameOverMaxLengthThrowsException() {
        User user = this.createUser();

        assertThrows(IllegalArgumentException.class, () -> user.setName("a".repeat(51)));
    }

    @Test
    void testSetEmailWithInvalidValueThrowsException() {
        User user = this.createUser();

        assertThrows(IllegalArgumentException.class, () -> user.setEmail(null));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("spotify.com"));
    }

    @Test
    void testSetEmailStoresValidValue() {
        User user = this.createUser();

        user.setEmail("nuevo@spotify.com");

        assertEquals("nuevo@spotify.com", user.getEmail());
    }

    @Test
    void testSetIdStoresValue() {
        User user = this.createUser();

        user.setId(42L);

        assertEquals(42L, user.getId());
    }

    @Test
    void testSetDateOfBirthWithNullThrowsExceptionInsteadOfNullPointerException() {
        User user = this.createUser();

        assertThrows(IllegalArgumentException.class, () -> user.setDateOfBirth(null));
    }

    @Test
    void testSetDateOfBirthBeforeMinYearThrowsException() {
        User user = this.createUser();

        assertThrows(IllegalArgumentException.class,
                () -> user.setDateOfBirth(LocalDate.of(1899, 6, 15)));
    }

    @Test
    void testSetDateOfBirthAtMinYearStoresValue() {
        User user = this.createUser();
        LocalDate minYearDate = LocalDate.of(1900, 1, 1);

        user.setDateOfBirth(minYearDate);

        assertEquals(minYearDate, user.getDateOfBirth());
    }

    @Test
    void testSetGenderWithNullThrowsException() {
        User user = this.createUser();

        assertThrows(IllegalArgumentException.class, () -> user.setGender(null));
    }

    @Test
    void testSetGenderStoresValue() {
        User user = this.createUser();

        user.setGender(User.Gender.NON_BINARY);

        assertEquals(User.Gender.NON_BINARY, user.getGender());
    }

    @Test
    void testSetActiveWithFalseStoresValue() {
        User user = this.createUser();

        user.setActive(false);

        assertFalse(user.isActive());
    }

    @Test
    void testSetActiveWithNullRestoresDefaultValue() {
        User user = this.createUser();

        user.setActive(false);
        user.setActive(null);

        assertTrue(user.isActive());
    }

    @Test
    void testEqualsWithSameIdIsTrueEvenWhenOtherDataDiffers() {
        User user = this.createUser();
        User sameId = this.createUser("ana@spotify.com");
        user.setId(7L);
        sameId.setId(7L);

        assertEquals(user, sameId);
        assertEquals(user.hashCode(), sameId.hashCode());
        assertEquals(user, user);
    }

    @Test
    void testEqualsWithDifferentIdIsFalse() {
        User user = this.createUser();
        User other = this.createUser(EMAIL);
        user.setId(7L);
        other.setId(8L);

        assertNotEquals(user, other);
    }

    @Test
    void testEqualsWithNullIdIsFalseForDifferentInstances() {
        User user = this.createUser();
        User other = this.createUser(EMAIL);

        assertNotEquals(user, other);
        assertEquals(user, user);
    }

    @Test
    void testEqualsWithNullOrOtherTypeIsFalse() {
        User user = this.createUser();

        assertNotEquals(user, null);
        assertNotEquals(user, "daniel@spotify.com");
    }

    @Test
    void testToStringContainsUserData() {
        User user = this.createUser();
        user.setId(7L);

        String text = user.toString();

        assertTrue(text.contains("id=7"));
        assertTrue(text.contains(NAME));
        assertTrue(text.contains(EMAIL));
    }
}
