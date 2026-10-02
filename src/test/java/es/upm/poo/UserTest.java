package es.upm.poo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void testConstructorStoresValidValues() {
        User user = new User("Daniel", "daniel@spotify.com", 30);

        assertEquals("Daniel", user.getName());
        assertEquals("daniel@spotify.com", user.getEmail());
        assertEquals(30, user.getAge());
        assertTrue(user.isActive());
    }

    @Test
    void testConstructorWithInvalidNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new User(null, "daniel@spotify.com", 30));
        assertThrows(IllegalArgumentException.class, () -> new User("", "daniel@spotify.com", 30));
        assertThrows(IllegalArgumentException.class, () -> new User("   ", "daniel@spotify.com", 30));
    }

    @Test
    void testSetNameStoresValue() {
        User user = new User("Daniel", "daniel@spotify.com", 30);
        user.setName("Ana");
        assertEquals("Ana", user.getName());
    }

    @Test
    void testSetEmailWithInvalidValueThrowsException() {
        User user = new User("Daniel", "daniel@spotify.com", 30);
        assertThrows(IllegalArgumentException.class, () -> user.setEmail(null));
        assertThrows(IllegalArgumentException.class, () -> user.setEmail("spotify.com"));
    }

    @Test
    void testSetEmailStoresValidValue() {
        User user = new User("Daniel", "daniel@spotify.com", 30);
        user.setEmail("nuevo@spotify.com");
        assertEquals("nuevo@spotify.com", user.getEmail());
    }

    @Test
    void testSetAgeWithInvalidValueThrowsException() {
        User user = new User("Daniel", "daniel@spotify.com", 30);
        assertThrows(IllegalArgumentException.class, () -> user.setAge(null));
        assertThrows(IllegalArgumentException.class, () -> user.setAge(12));
    }

    @Test
    void testSetAgeAtMinimumStoresValue() {
        User user = new User("Daniel", "daniel@spotify.com", 30);
        user.setAge(13);
        assertEquals(13, user.getAge());
    }

    @Test
    void testSetGenderWithNullThrowsException() {
        User user = new User("Daniel", "daniel@spotify.com", 30);
        assertThrows(IllegalArgumentException.class, () -> user.setGender(null));
    }

    @Test
    void testSetGenderStoresValue() {
        User user = new User("Daniel", "daniel@spotify.com", 30);
        user.setGender(User.Gender.NON_BINARY);
        assertEquals(User.Gender.NON_BINARY, user.getGender());
    }

    @Test
    void testSetActiveWithFalseStoresValue() {
        User user = new User("Daniel", "daniel@spotify.com", 30);
        user.setActive(false);
        assertFalse(user.isActive());
    }

    @Test
    void testSetActiveWithNullRestoresDefaultValue() {
        User user = new User("Daniel", "daniel@spotify.com", 30);
        user.setActive(false);
        user.setActive(null);
        assertTrue(user.isActive());
    }
}
