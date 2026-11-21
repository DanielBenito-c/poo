package es.upm.poo.data.repositories.map;

import es.upm.poo.data.model.Park;
import es.upm.poo.data.repositories.ParkRepository;

public class ParkRepositoryMap extends GenericRepositoryMap<Park> implements ParkRepository {

    @Override
    public void setId(Park entity, long id) {
        entity.setId(id);
    }
}
