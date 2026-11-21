package es.upm.poo.data.repositories.map;

import es.upm.poo.data.model.Attraction;
import es.upm.poo.data.repositories.AttractionRepository;

public class AttractionRepositoryMap extends GenericRepositoryMap<Attraction> implements AttractionRepository {

    @Override
    public void setId(Attraction entity, long id) {
        entity.setId(id);
    }
}
