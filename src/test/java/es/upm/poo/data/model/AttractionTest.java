package es.upm.poo.data.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttractionTest {

    private static final String NAME = "Space Mountain";
    private static final int MIN_HEIGHT_CM = 120;
    private static final int CAPACITY = 200;

    private Attraction createAttraction() {
        return new Attraction(NAME, MIN_HEIGHT_CM, CAPACITY);
    }

    @Test
    void testConstructorStoresValuesAndLeavesIdUnset() {
        Attraction attraction = this.createAttraction();

        assertNull(attraction.getId());
        assertEquals(NAME, attraction.getName());
        assertEquals(MIN_HEIGHT_CM, attraction.getMinHeightCm());
        assertEquals(CAPACITY, attraction.getCapacity());
    }

    @Test
    void testConstructorAllowsMissingMinHeight() {
        Attraction attraction = new Attraction(NAME, null, CAPACITY);

        assertNull(attraction.getMinHeightCm());
    }

    @Test
    void testConstructorRejectsNullOrBlankName() {
        IllegalArgumentException nullName = assertThrows(IllegalArgumentException.class,
                () -> new Attraction(null, MIN_HEIGHT_CM, CAPACITY));
        IllegalArgumentException blankName = assertThrows(IllegalArgumentException.class,
                () -> new Attraction("   ", MIN_HEIGHT_CM, CAPACITY));

        assertTrue(nullName.getMessage().contains("name"));
        assertTrue(blankName.getMessage().contains("name"));
    }

    @Test
    void testConstructorRejectsNegativeMinHeight() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Attraction(NAME, -1, CAPACITY));

        assertTrue(exception.getMessage().contains("minHeightCm"));
    }

    @Test
    void testConstructorRejectsNullOrNonPositiveCapacity() {
        IllegalArgumentException nullCapacity = assertThrows(IllegalArgumentException.class,
                () -> new Attraction(NAME, MIN_HEIGHT_CM, null));
        IllegalArgumentException zeroCapacity = assertThrows(IllegalArgumentException.class,
                () -> new Attraction(NAME, MIN_HEIGHT_CM, 0));
        IllegalArgumentException negativeCapacity = assertThrows(IllegalArgumentException.class,
                () -> new Attraction(NAME, MIN_HEIGHT_CM, -5));

        assertTrue(nullCapacity.getMessage().contains("capacity"));
        assertTrue(zeroCapacity.getMessage().contains("capacity"));
        assertTrue(negativeCapacity.getMessage().contains("capacity"));
    }

    @Test
    void testSetNameStoresValue() {
        Attraction attraction = this.createAttraction();

        attraction.setName("Thunder");

        assertEquals("Thunder", attraction.getName());
    }

    @Test
    void testSetMinHeightCmStoresValueAndAllowsNull() {
        Attraction attraction = this.createAttraction();

        attraction.setMinHeightCm(140);
        assertEquals(140, attraction.getMinHeightCm());

        attraction.setMinHeightCm(null);
        assertNull(attraction.getMinHeightCm());
    }

    @Test
    void testSetCapacityStoresValue() {
        Attraction attraction = this.createAttraction();

        attraction.setCapacity(50);

        assertEquals(50, attraction.getCapacity());
    }

    @Test
    void testToStringContainsAttractionData() {
        Attraction attraction = this.createAttraction();
        attraction.setId(7L);

        String text = attraction.toString();

        assertTrue(text.contains("id=7"));
        assertTrue(text.contains(NAME));
        assertTrue(text.contains("minHeightCm=" + MIN_HEIGHT_CM));
        assertTrue(text.contains("capacity=" + CAPACITY));
    }
}
