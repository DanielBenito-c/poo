package es.upm.poo.services;

import es.upm.poo.data.model.Attraction;
import es.upm.poo.data.repositories.AttractionRepository;
import es.upm.poo.data.repositories.ZoneRepository;

import java.util.List;

public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final ZoneRepository zoneRepository;

    public AttractionService(AttractionRepository attractionRepository, ZoneRepository zoneRepository) {
        this.attractionRepository = attractionRepository;
        this.zoneRepository = zoneRepository;
    }

    public Attraction create(Attraction attraction) {
        return this.attractionRepository.create(attraction);
    }

    public List<Attraction> findAll() {
        return this.attractionRepository.findAll();
    }
}
