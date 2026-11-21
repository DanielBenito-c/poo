package es.upm.poo.services;

import es.upm.poo.data.model.Zone;
import es.upm.poo.data.repositories.map.ParkRepositoryMap;
import es.upm.poo.data.repositories.map.ZoneRepositoryMap;
import es.upm.poo.services.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZoneServiceIT {

    private ZoneService createZoneService() {
        return new ZoneService(new ZoneRepositoryMap(), new ParkRepositoryMap());
    }

    @Test
    void testCreatePersistsZoneAndAssignsId() {
        ZoneRepositoryMap zoneRepository = new ZoneRepositoryMap();
        ZoneService service = new ZoneService(zoneRepository, new ParkRepositoryMap());

        Zone createdZone = service.create(new Zone("Aventura", "Emociones"));

        assertEquals(1L, createdZone.getId());
        assertSame(createdZone, zoneRepository.read(1L).orElseThrow());
    }

    @Test
    void testFindAllReturnsCreatedZones() {
        ZoneService service = this.createZoneService();

        service.create(new Zone("Aventura", "Emociones"));
        service.create(new Zone("Infantil", "Familiar"));

        assertEquals(2, service.findAll().size());
    }

    @Test
    void testFindAllReturnsEmptyListInitially() {
        ZoneService service = this.createZoneService();

        assertTrue(service.findAll().isEmpty());
    }

    @Test
    void testReadZoneReturnsRequestedZone() {
        ZoneService service = this.createZoneService();
        Zone expectedZone = service.create(new Zone("Aventura", "Emociones"));

        assertSame(expectedZone, service.readZone(expectedZone.getId()));
    }

    @Test
    void testReadUnknownZoneThrowsNotFoundExceptionWithId() {
        ZoneService service = this.createZoneService();

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.readZone(42L));

        assertTrue(exception.getMessage().contains("42"));
    }
}
