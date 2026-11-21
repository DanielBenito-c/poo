package es.upm.poo.presentation.cli;

import es.upm.poo.data.model.Consent;
import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;

public class CommandParams {
    private final String[] params;

    public CommandParams(String[] params) {
        this.params = params;
    }

    public String getString(Integer index) {
        return this.getParam(index, value -> value);
    }

    public Integer getInteger(Integer index) {
        return this.getParam(index, Integer::valueOf);
    }

    public Long getLong(Integer index) {
        return this.getParam(index, Long::valueOf);
    }

    public Double getDouble(Integer index) {
        return this.getParam(index, Double::valueOf);
    }

    public BigDecimal getBigDecimal(Integer index) {
        return this.getParam(index, BigDecimal::new);
    }

    public LocalDate getLocalDate(Integer index) {
        return this.getParam(index, LocalDate::parse); //yyyy-MM-dd
    }

    public LocalDateTime getLocalDateTime(Integer index) {return this.getParam(index, LocalDateTime::parse);}

    public LocalTime getLocalTime(Integer index) {
        return this.getParam(index, LocalTime::parse);
    }

    public Boolean getBoolean(Integer index) {
        return this.getParam(index, Boolean::valueOf);
    }

    public User.Gender getGender(Integer index) {
        return this.getParam(index, User.Gender::valueOf);
    }

    public Consent.ConsentPurpose getConsentPurpose(Integer index) {return this.getParam(index,Consent.ConsentPurpose::valueOf);}

    public Ticket.TicketType getTicketType(Integer index) {
        return this.getParam(index, Ticket.TicketType::valueOf);
    }

    private <T> T getParam(Integer index, Function<String, T> converter) {
        if (this.params.length <= index) {
            return null;
        }
        return converter.apply(this.params[index]);
    }
}

