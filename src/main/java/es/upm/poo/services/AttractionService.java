package es.upm.poo.services;

import es.upm.poo.data.repositories.AttractionRepository;
import es.upm.poo.data.repositories.ZoneRepository;

public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final ZoneRepository zoneRepository;

    public AttractionService(AttractionRepository attractionRepository, ZoneRepository zoneRepository) {
        this.attractionRepository = attractionRepository;
        this.zoneRepository = zoneRepository;
    }
}
