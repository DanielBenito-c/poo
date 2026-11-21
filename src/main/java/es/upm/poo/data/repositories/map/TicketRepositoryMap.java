package es.upm.poo.data.repositories.map;

import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.repositories.TicketRepository;

public class TicketRepositoryMap extends GenericRepositoryMap<Ticket> implements TicketRepository {

    @Override
    public void setId(Ticket entity, long id) {
        entity.setId(id);
    }
}
