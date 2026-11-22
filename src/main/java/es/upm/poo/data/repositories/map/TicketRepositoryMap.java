package es.upm.poo.data.repositories.map;

import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.repositories.TicketRepository;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TicketRepositoryMap extends GenericRepositoryMap<Ticket> implements TicketRepository {

    @Override
    public void setId(Ticket entity, long id) {
        entity.setId(id);
    }

    @Override
    public List<Ticket> findByUserNameAndParkNameAndTicketType(String userName, String parkName,
                                                               Ticket.TicketType ticketType) {
        String normalizedUserName = this.normalize(userName);
        String normalizedParkName = this.normalize(parkName);
        return this.findAll().stream()
                .filter(ticket -> ticket.getUser() != null
                        && this.matches(ticket.getUser().getName(), normalizedUserName))
                .filter(ticket -> ticket.getPark() != null
                        && this.matches(ticket.getPark().getName(), normalizedParkName))
                .filter(ticket -> ticket.getTicketType() == ticketType)
                .toList();
    }

    @Override
    public Map<String, Long> countVisitsByYear(int year) {
        return this.findAll().stream()
                .filter(ticket -> ticket.getUser() != null
                        && ticket.getValidDate() != null
                        && ticket.getValidDate().getYear() == year)
                .collect(Collectors.groupingBy(ticket -> ticket.getUser().getEmail(),
                        TreeMap::new, Collectors.counting()));
    }

    private String normalize(String value) {
        return value == null ? "" : value.toLowerCase(Locale.ROOT);
    }

    private boolean matches(String value, String normalizedExpected) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(normalizedExpected);
    }
}
