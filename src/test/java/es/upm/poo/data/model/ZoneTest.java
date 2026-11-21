package es.upm.poo.data.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZoneTest {

    private static final String NAME = "Aventura";
    private static final String THEME = "Emociones";

    private Zone createZone() {
        return new Zone(NAME, THEME);
    }

    @Test
    void testConstructorStoresValuesAndLeavesIdAndAttractionsUnset() {
        Zone zone = this.createZone();

        assertNull(zone.getId());
        assertEquals(NAME, zone.getName());
        assertEquals(THEME, zone.getTheme());
        assertTrue(zone.getAttractions().isEmpty());
    }

    @Test
    void testConstructorRejectsNullOrBlankName() {
        IllegalArgumentException nullName = assertThrows(IllegalArgumentException.class,
                () -> new Zone(null, THEME));
        IllegalArgumentException blankName = assertThrows(IllegalArgumentException.class,
                () -> new Zone("   ", THEME));

        assertTrue(nullName.getMessage().contains("name"));
        assertTrue(blankName.getMessage().contains("name"));
    }

    @Test
    void testConstructorRejectsNullOrBlankTheme() {
        IllegalArgumentException nullTheme = assertThrows(IllegalArgumentException.class,
                () -> new Zone(NAME, null));
        IllegalArgumentException blankTheme = assertThrows(IllegalArgumentException.class,
                () -> new Zone(NAME, "   "));

        assertTrue(nullTheme.getMessage().contains("theme"));
        assertTrue(blankTheme.getMessage().contains("theme"));
    }

    @Test
    void testAddAttractionStoresAttraction() {
        Zone zone = this.createZone();
        Attraction attraction = new Attraction("Thunder", 120, 50);

        zone.addAttraction(attraction);

        assertEquals(1, zone.getAttractions().size());
        assertSame(attraction, zone.getAttractions().get(0));
    }

    @Test
    void testAddAttractionWithNullThrowsException() {
        Zone zone = this.createZone();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> zone.addAttraction(null));

        assertTrue(exception.getMessage().contains("attraction"));
    }

    @Test
    void testAddAttractionWithSameInstanceTwiceThrowsException() {
        Zone zone = this.createZone();
        Attraction attraction = new Attraction("Thunder", 120, 50);
        zone.addAttraction(attraction);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> zone.addAttraction(attraction));

        assertTrue(exception.getMessage().contains("attraction"));
        assertEquals(1, zone.getAttractions().size());
    }

    @Test
    void testGetAttractionsIsUnmodifiable() {
        Zone zone = this.createZone();

        assertThrows(UnsupportedOperationException.class,
                () -> zone.getAttractions().add(new Attraction("Thunder", 120, 50)));
    }

    @Test
    void testSettersUpdateZoneData() {
        Zone zone = this.createZone();

        zone.setName("Infantil");
        zone.setTheme("Familiar");

        assertEquals("Infantil", zone.getName());
        assertEquals("Familiar", zone.getTheme());
    }

    @Test
    void testToStringContainsZoneDataAndAttractionNames() {
        Zone zone = this.createZone();
        zone.setId(4L);
        zone.addAttraction(new Attraction("Thunder", 120, 50));

        String text = zone.toString();

        assertTrue(text.contains("id=4"));
        assertTrue(text.contains(NAME));
        assertTrue(text.contains(THEME));
        assertTrue(text.contains("Thunder"));
    }
}
