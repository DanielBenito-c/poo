package es.upm.poo.services;

import es.upm.poo.data.repositories.ParkRepository;
import es.upm.poo.data.repositories.ZoneRepository;

public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final ParkRepository parkRepository;

    public ZoneService(ZoneRepository zoneRepository, ParkRepository parkRepository) {
        this.zoneRepository = zoneRepository;
        this.parkRepository = parkRepository;
    }
}
