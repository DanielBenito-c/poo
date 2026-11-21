package es.upm.poo.presentation.cli;

import es.upm.poo.data.model.Consent;
import es.upm.poo.data.model.Ticket;
import es.upm.poo.data.model.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandParamsTest {

    @Test
    void testGettersConvertValuesToExpectedTypes() {
        CommandParams params = new CommandParams(new String[]{
                "text", "12", "34", "1.5", "2.50", "2000-01-01",
                "2026-11-01T12:30:00", "true", "FEMALE", "MARKETING"
        });

        assertEquals("text", params.getString(0));
        assertEquals(12, params.getInteger(1));
        assertEquals(34L, params.getLong(2));
        assertEquals(1.5, params.getDouble(3), 0.0001);
        assertEquals(new BigDecimal("2.50"), params.getBigDecimal(4));
        assertEquals(LocalDate.of(2000, 1, 1), params.getLocalDate(5));
        assertEquals(LocalDateTime.of(2026, 11, 1, 12, 30), params.getLocalDateTime(6));
        assertTrue(params.getBoolean(7));
        assertEquals(User.Gender.FEMALE, params.getGender(8));
        assertEquals(Consent.ConsentPurpose.MARKETING, params.getConsentPurpose(9));
    }

    @Test
    void testGetTicketTypeConvertsValue() {
        CommandParams params = new CommandParams(new String[]{"FAST_PASS"});

        assertEquals(Ticket.TicketType.FAST_PASS, params.getTicketType(0));
    }

    @Test
    void testGetLocalTimeConvertsValue() {
        CommandParams params = new CommandParams(new String[]{"09:30"});

        assertEquals(LocalTime.of(9, 30), params.getLocalTime(0));
    }

    @Test
    void testGetterReturnsNullForMissingOptionalParameter() {
        CommandParams params = new CommandParams(new String[0]);

        assertNull(params.getString(0));
        assertNull(params.getLong(0));
    }

    @Test
    void testAllGettersReturnNullForMissingOptionalParameter() {
        CommandParams params = new CommandParams(new String[0]);

        assertNull(params.getInteger(0));
        assertNull(params.getDouble(0));
        assertNull(params.getBigDecimal(0));
        assertNull(params.getLocalDate(0));
        assertNull(params.getLocalDateTime(0));
        assertNull(params.getLocalTime(0));
        assertNull(params.getBoolean(0));
        assertNull(params.getGender(0));
        assertNull(params.getConsentPurpose(0));
        assertNull(params.getTicketType(0));
    }

    @Test
    void testGetBooleanConvertsFalseValue() {
        CommandParams params = new CommandParams(new String[]{"false"});

        assertFalse(params.getBoolean(0));
    }

    @Test
    void testGetGenderWithUnknownValueThrowsIllegalArgumentException() {
        CommandParams params = new CommandParams(new String[]{"UNKNOWN"});

        assertThrows(IllegalArgumentException.class, () -> params.getGender(0));
    }

    @Test
    void testGetLocalDateWithInvalidValueThrowsDateTimeParseException() {
        CommandParams params = new CommandParams(new String[]{"01/01/2026"});

        assertThrows(DateTimeParseException.class, () -> params.getLocalDate(0));
    }

    @Test
    void testGetIntegerWithInvalidValueThrowsNumberFormatException() {
        CommandParams params = new CommandParams(new String[]{"not-a-number"});

        assertThrows(NumberFormatException.class, () -> params.getInteger(0));
    }
}
