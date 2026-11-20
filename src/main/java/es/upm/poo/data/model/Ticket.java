package es.upm.poo.data.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Ticket {

    private Long id;
    private User user;
    private Park park;
    private TicketType ticketType;
    private BigDecimal price;
    private LocalDate purchaseDate;
    private LocalDate validDate;

    public Ticket(TicketType ticketType, BigDecimal price, LocalDate purchaseDate,LocalDate validDate) {
        this.setTicketType(ticketType);
        this.setPrice(price);
        this.setPurchaseDate(purchaseDate);
        this.setValidDate(validDate);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user es obligatorio");
        }
        this.user = user;
    }

    public Park getPark() {
        return this.park;
    }

    public void setPark(Park park) {
        if (park == null) {
            throw new IllegalArgumentException("park es obligatorio");
        }
        this.park = park;
    }

    public TicketType getTicketType() {
        return this.ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        if (ticketType == null) {
            throw new IllegalArgumentException("ticketType es obligatorio");
        }
        this.ticketType = ticketType;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("price es obligatorio");
        }
        if (price.signum() < 0) {
            throw  new IllegalArgumentException("price no puede ser negativo");
        }
        this.price = price;
    }

    public LocalDate getPurchaseDate() {
        return this.purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        if (purchaseDate == null) {
            throw new IllegalArgumentException("purchaseDate es obligatorio");
        }
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getValidDate() {
        return this.validDate;
    }

    public void setValidDate(LocalDate validDate) {
        if (validDate == null) {
            throw new IllegalArgumentException("validDate es obligatorio");
        }
        if (validDate.isBefore(this.purchaseDate)) {
            throw new IllegalArgumentException("validDate no puede ser anterior a purchaseDate");
        }
        this.validDate = validDate;
    }

    public enum TicketType {
        STANDARD,
        FAST_PASS
    }
}
