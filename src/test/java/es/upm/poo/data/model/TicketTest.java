package es.upm.poo.data.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicketTest {

    private static final BigDecimal PRICE = new BigDecimal("50.00");
    private static final LocalDate PURCHASE_DATE = LocalDate.of(2026, 1, 1);
    private static final LocalDate VALID_DATE = LocalDate.of(2026, 1, 2);

    private Ticket createTicket() {
        return new Ticket(Ticket.TicketType.STANDARD, PRICE, PURCHASE_DATE, VALID_DATE);
    }

    private User createUser(long id) {
        User user = new User("Alice", "alice@upm.es", LocalDate.of(1996, 1, 1),
                User.Gender.FEMALE, true);
        user.setId(id);
        return user;
    }

    private Park createPark(long id) {
        Park park = new Park("Park", "Address", LocalTime.of(9, 0),
                LocalTime.of(20, 0), "Zone", "Theme");
        park.setId(id);
        return park;
    }

    @Test
    void testConstructorStoresValuesAndLeavesAssociationsUnset() {
        Ticket ticket = this.createTicket();

        assertNull(ticket.getId());
        assertNull(ticket.getUser());
        assertNull(ticket.getPark());
        assertEquals(Ticket.TicketType.STANDARD, ticket.getTicketType());
        assertEquals(PRICE, ticket.getPrice());
        assertEquals(PURCHASE_DATE, ticket.getPurchaseDate());
        assertEquals(VALID_DATE, ticket.getValidDate());
    }

    @Test
    void testConstructorRejectsNullTicketType() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Ticket(null, PRICE, PURCHASE_DATE, VALID_DATE));

        assertTrue(exception.getMessage().contains("ticketType"));
    }

    @Test
    void testConstructorRejectsNullOrNegativePrice() {
        IllegalArgumentException nullPrice = assertThrows(IllegalArgumentException.class,
                () -> new Ticket(Ticket.TicketType.STANDARD, null, PURCHASE_DATE, VALID_DATE));
        IllegalArgumentException negativePrice = assertThrows(IllegalArgumentException.class,
                () -> new Ticket(Ticket.TicketType.STANDARD, new BigDecimal("-0.01"),
                        PURCHASE_DATE, VALID_DATE));

        assertTrue(nullPrice.getMessage().contains("price"));
        assertTrue(negativePrice.getMessage().contains("price"));
    }

    @Test
    void testConstructorAcceptsZeroPrice() {
        Ticket ticket = new Ticket(Ticket.TicketType.STANDARD, BigDecimal.ZERO,
                PURCHASE_DATE, VALID_DATE);

        assertEquals(BigDecimal.ZERO, ticket.getPrice());
    }

    @Test
    void testConstructorRejectsNullPurchaseDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Ticket(Ticket.TicketType.STANDARD, PRICE, null, VALID_DATE));

        assertTrue(exception.getMessage().contains("purchaseDate"));
    }

    @Test
    void testConstructorRejectsNullValidDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Ticket(Ticket.TicketType.STANDARD, PRICE, PURCHASE_DATE, null));

        assertTrue(exception.getMessage().contains("validDate"));
    }

    @Test
    void testConstructorRejectsValidDateBeforePurchaseDate() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Ticket(Ticket.TicketType.STANDARD, PRICE, VALID_DATE, PURCHASE_DATE));

        assertTrue(exception.getMessage().contains("validDate"));
    }

    @Test
    void testConstructorAcceptsValidDateEqualToPurchaseDate() {
        Ticket ticket = new Ticket(Ticket.TicketType.STANDARD, PRICE,
                PURCHASE_DATE, PURCHASE_DATE);

        assertEquals(PURCHASE_DATE, ticket.getValidDate());
    }

    @Test
    void testSetUserStoresValueAndRejectsNull() {
        Ticket ticket = this.createTicket();
        User user = this.createUser(1L);

        ticket.setUser(user);

        assertSame(user, ticket.getUser());
        assertThrows(IllegalArgumentException.class, () -> ticket.setUser(null));
    }

    @Test
    void testSetParkStoresValueAndRejectsNull() {
        Ticket ticket = this.createTicket();
        Park park = this.createPark(2L);

        ticket.setPark(park);

        assertSame(park, ticket.getPark());
        assertThrows(IllegalArgumentException.class, () -> ticket.setPark(null));
    }

    @Test
    void testSetPurchaseDateBeforeValidDateStoresValue() {
        Ticket ticket = this.createTicket();

        ticket.setPurchaseDate(LocalDate.of(2025, 12, 31));

        assertEquals(LocalDate.of(2025, 12, 31), ticket.getPurchaseDate());
    }

    @Test
    void testSetPurchaseDateAfterValidDateThrowsException() {
        Ticket ticket = this.createTicket();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ticket.setPurchaseDate(LocalDate.of(2026, 1, 3)));

        assertTrue(exception.getMessage().contains("purchaseDate"));
        assertEquals(PURCHASE_DATE, ticket.getPurchaseDate());
    }

    @Test
    void testSettersUpdateTicketValues() {
        Ticket ticket = this.createTicket();

        ticket.setTicketType(Ticket.TicketType.FAST_PASS);
        ticket.setPrice(new BigDecimal("99.00"));
        ticket.setValidDate(LocalDate.of(2026, 1, 4));
        ticket.setPurchaseDate(LocalDate.of(2026, 1, 3));

        assertEquals(Ticket.TicketType.FAST_PASS, ticket.getTicketType());
        assertEquals(new BigDecimal("99.00"), ticket.getPrice());
        assertEquals(LocalDate.of(2026, 1, 3), ticket.getPurchaseDate());
        assertEquals(LocalDate.of(2026, 1, 4), ticket.getValidDate());
    }

    @Test
    void testSetValidDateBeforePurchaseDateThrowsException() {
        Ticket ticket = this.createTicket();

        assertThrows(IllegalArgumentException.class,
                () -> ticket.setValidDate(LocalDate.of(2025, 12, 31)));

        assertEquals(VALID_DATE, ticket.getValidDate());
    }

    @Test
    void testToStringContainsTicketData() {
        Ticket ticket = this.createTicket();
        ticket.setId(5L);
        ticket.setUser(this.createUser(1L));
        ticket.setPark(this.createPark(2L));

        String text = ticket.toString();

        assertTrue(text.contains("id=5"));
        assertTrue(text.contains("userId=1"));
        assertTrue(text.contains("parkId=2"));
        assertTrue(text.contains("ticketType=STANDARD"));
        assertTrue(text.contains("price=" + PRICE));
        assertTrue(text.contains(PURCHASE_DATE.toString()));
        assertTrue(text.contains(VALID_DATE.toString()));
    }
}
