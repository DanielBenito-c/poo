package es.upm.poo.data.model;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParkTest {

    private static final String NAME = "Magic Kingdom";
    private static final String ADDRESS = "Orlando, Florida";
    private static final LocalTime OPENING_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(22, 0);
    private static final String ZONE_NAME = "Fantasyland";
    private static final String ZONE_THEME = "Fantasy";

    private Park createPark() {
        return new Park(NAME, ADDRESS, OPENING_TIME, CLOSING_TIME, ZONE_NAME, ZONE_THEME);
    }

    @Test
    void testConstructorStoresValuesAndCreatesInitialZone() {
        Park park = this.createPark();

        assertNull(park.getId());
        assertEquals(NAME, park.getName());
        assertEquals(ADDRESS, park.getAddress());
        assertEquals(OPENING_TIME, park.getOpeningTime());
        assertEquals(CLOSING_TIME, park.getClosingTime());
        assertEquals(1, park.getZones().size());
        Zone zone = park.getZones().get(0);
        assertEquals(ZONE_NAME, zone.getName());
        assertEquals(ZONE_THEME, zone.getTheme());
        assertNull(zone.getId());
    }

    @Test
    void testConstructorRejectsNullOrBlankName() {
        IllegalArgumentException nullName = assertThrows(IllegalArgumentException.class,
                () -> new Park(null, ADDRESS, OPENING_TIME, CLOSING_TIME, ZONE_NAME, ZONE_THEME));
        IllegalArgumentException blankName = assertThrows(IllegalArgumentException.class,
                () -> new Park("   ", ADDRESS, OPENING_TIME, CLOSING_TIME, ZONE_NAME, ZONE_THEME));

        assertTrue(nullName.getMessage().contains("name"));
        assertTrue(blankName.getMessage().contains("name"));
    }

    @Test
    void testConstructorRejectsNullOrBlankAddress() {
        IllegalArgumentException nullAddress = assertThrows(IllegalArgumentException.class,
                () -> new Park(NAME, null, OPENING_TIME, CLOSING_TIME, ZONE_NAME, ZONE_THEME));
        IllegalArgumentException blankAddress = assertThrows(IllegalArgumentException.class,
                () -> new Park(NAME, "   ", OPENING_TIME, CLOSING_TIME, ZONE_NAME, ZONE_THEME));

        assertTrue(nullAddress.getMessage().contains("address"));
        assertTrue(blankAddress.getMessage().contains("address"));
    }

    @Test
    void testConstructorRejectsNullOpeningTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Park(NAME, ADDRESS, null, CLOSING_TIME, ZONE_NAME, ZONE_THEME));

        assertTrue(exception.getMessage().contains("openingTime"));
    }

    @Test
    void testConstructorRejectsNullClosingTime() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Park(NAME, ADDRESS, OPENING_TIME, null, ZONE_NAME, ZONE_THEME));

        assertTrue(exception.getMessage().contains("closingTime"));
    }

    @Test
    void testAddZoneAppendsZoneAndReturnsIt() {
        Park park = this.createPark();

        Zone zone = park.addZone("Tomorrowland", "Space");

        assertEquals(2, park.getZones().size());
        assertSame(zone, park.getZones().get(1));
        assertEquals("Tomorrowland", zone.getName());
        assertEquals("Space", zone.getTheme());
    }

    @Test
    void testGetZonesIsUnmodifiable() {
        Park park = this.createPark();

        assertThrows(UnsupportedOperationException.class,
                () -> park.getZones().add(new Zone("Tomorrowland", "Space")));
    }

    @Test
    void testSettersUpdateParkData() {
        Park park = this.createPark();

        park.setName("Epcot");
        park.setAddress("Orlando, Florida, USA");
        park.setOpeningTime(LocalTime.of(10, 0));
        park.setClosingTime(LocalTime.of(21, 0));

        assertEquals("Epcot", park.getName());
        assertEquals("Orlando, Florida, USA", park.getAddress());
        assertEquals(LocalTime.of(10, 0), park.getOpeningTime());
        assertEquals(LocalTime.of(21, 0), park.getClosingTime());
    }

    @Test
    void testToStringContainsParkDataAndZoneNames() {
        Park park = this.createPark();
        park.setId(3L);
        park.addZone("Tomorrowland", "Space");

        String text = park.toString();

        assertTrue(text.contains("id=3"));
        assertTrue(text.contains(NAME));
        assertTrue(text.contains(ADDRESS));
        assertTrue(text.contains(ZONE_NAME));
        assertTrue(text.contains("Tomorrowland"));
    }
}
