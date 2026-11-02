package es.upm.poo.data.repositories.map;

import es.upm.poo.data.model.Consent;
import es.upm.poo.data.repositories.ConsentRepository;

public class ConsentRepositoryMap extends GenericRepositoryMap<Consent> implements ConsentRepository {


    @Override
    public void setId(Consent entity, long id) {
        entity.setId(id);
    }
}
