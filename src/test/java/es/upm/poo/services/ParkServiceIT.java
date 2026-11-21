package es.upm.poo.services;

import es.upm.poo.data.model.Park;
import es.upm.poo.data.repositories.map.ParkRepositoryMap;
import es.upm.poo.services.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParkServiceIT {

    private Park createPark(String name) {
        return new Park(name, "Address", LocalTime.of(9, 0), LocalTime.of(20, 0),
                "Zone", "Theme");
    }

    @Test
    void testCreatePersistsParkAndAssignsId() {
        ParkRepositoryMap parkRepository = new ParkRepositoryMap();
        ParkService service = new ParkService(parkRepository);

        Park createdPark = service.create(this.createPark("Park"));

        assertEquals(1L, createdPark.getId());
        assertEquals(1, createdPark.getZones().size());
        assertSame(createdPark, parkRepository.read(1L).orElseThrow());
    }

    @Test
    void testFindAllReturnsCreatedParks() {
        ParkService service = new ParkService(new ParkRepositoryMap());

        service.create(this.createPark("Park"));
        service.create(this.createPark("Second Park"));

        assertEquals(2, service.findAll().size());
    }

    @Test
    void testFindAllReturnsEmptyListInitially() {
        ParkService service = new ParkService(new ParkRepositoryMap());

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void testReadParkReturnsRequestedPark() {
        ParkService service = new ParkService(new ParkRepositoryMap());
        Park expectedPark = service.create(this.createPark("Park"));

        assertSame(expectedPark, service.readPark(expectedPark.getId()));
    }

    @Test
    void testReadUnknownParkThrowsNotFoundExceptionWithId() {
        ParkService service = new ParkService(new ParkRepositoryMap());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.readPark(42L));

        assertTrue(exception.getMessage().contains("42"));
    }
}
