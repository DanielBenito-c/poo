package es.upm.poo.services;

import es.upm.poo.data.model.Zone;
import es.upm.poo.data.repositories.ParkRepository;
import es.upm.poo.data.repositories.ZoneRepository;
import es.upm.poo.services.exceptions.NotFoundException;

import java.util.List;

public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final ParkRepository parkRepository;

    public ZoneService(ZoneRepository zoneRepository, ParkRepository parkRepository) {
        this.zoneRepository = zoneRepository;
        this.parkRepository = parkRepository;
    }

    public Zone create(Zone zone) {
        return this.zoneRepository.create(zone);
    }

    public List<Zone> findAll() {
        return this.zoneRepository.findAll();
    }

    public Zone readZone(Long id) {
        return this.zoneRepository.read(id)
                .orElseThrow(() -> new NotFoundException("zone not found with id: " + id));
    }
}
