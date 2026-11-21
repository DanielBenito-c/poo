package es.upm.poo.services;

import es.upm.poo.data.repositories.ParkRepository;

public class ParkService {

    private final ParkRepository parkRepository;

    public ParkService(ParkRepository parkRepository) {
        this.parkRepository = parkRepository;
    }
}
