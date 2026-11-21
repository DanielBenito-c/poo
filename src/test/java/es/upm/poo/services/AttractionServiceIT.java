package es.upm.poo.services;

import es.upm.poo.data.model.Attraction;
import es.upm.poo.data.repositories.map.AttractionRepositoryMap;
import es.upm.poo.data.repositories.map.ZoneRepositoryMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttractionServiceIT {

    private AttractionService createAttractionService() {
        return new AttractionService(new AttractionRepositoryMap(), new ZoneRepositoryMap());
    }

    @Test
    void testCreatePersistsAttractionAndAssignsId() {
        AttractionRepositoryMap attractionRepository = new AttractionRepositoryMap();
        AttractionService service = new AttractionService(attractionRepository, new ZoneRepositoryMap());

        Attraction createdAttraction = service.create(new Attraction("Thunder", 120, 50));

        assertEquals(1L, createdAttraction.getId());
        assertSame(createdAttraction, attractionRepository.read(1L).orElseThrow());
    }

    @Test
    void testFindAllReturnsCreatedAttractions() {
        AttractionService service = this.createAttractionService();

        service.create(new Attraction("Thunder", 120, 50));
        service.create(new Attraction("Carousel", null, 30));

        assertEquals(2, service.findAll().size());
    }

    @Test
    void testFindAllReturnsEmptyListInitially() {
        AttractionService service = this.createAttractionService();

        assertTrue(service.findAll().isEmpty());
    }
}
