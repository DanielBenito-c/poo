package es.upm.poo.data.repositories.map;

import es.upm.poo.data.model.Zone;
import es.upm.poo.data.repositories.ZoneRepository;

public class ZoneRepositoryMap extends GenericRepositoryMap<Zone> implements ZoneRepository {

    @Override
    public void setId(Zone entity, long id) {
        entity.setId(id);
    }
}
