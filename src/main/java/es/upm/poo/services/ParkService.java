package es.upm.poo.services;

import es.upm.poo.data.model.Park;
import es.upm.poo.data.repositories.ParkRepository;
import es.upm.poo.services.exceptions.NotFoundException;

import java.util.List;

public class ParkService {

    private final ParkRepository parkRepository;

    public ParkService(ParkRepository parkRepository) {
        this.parkRepository = parkRepository;
    }

    public Park create(Park park) {
        return this.parkRepository.create(park);
    }

    public List<Park> findAll() {
        return this.parkRepository.findAll();
    }

    public Park readPark(Long id) {
        return this.parkRepository.read(id)
                .orElseThrow(() -> new NotFoundException("park not found with id: " + id));
    }
}
