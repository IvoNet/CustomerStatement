package nl.ordina.rabobankcustomerstatement.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecordTest {

    @Test
    void isValid() {
        final var record = new Record(1L,
                                      "NL42FOOB0123456789",
                                      BigDecimal.ZERO,
                                      BigDecimal.TEN,
                                      "Test",
                                      BigDecimal.TEN);
        assertTrue(record.isValid());
    }

    @Test
    void isNotValid() {
        final var record = new Record(1L,
                                      "NL42FOOB0123456789",
                                      BigDecimal.ZERO,
                                      BigDecimal.TEN,
                                      "Test",
                                      BigDecimal.valueOf(42));
        assertFalse(record.isValid());
    }

    @Test
    void isValidNegative() {
        final var record = new Record(1L,
                                      "NL42FOOB0123456789",
                                      BigDecimal.ZERO,
                                      BigDecimal.valueOf(-10),
                                      "Test",
                                      BigDecimal.valueOf(-10));
        assertTrue(record.isValid());
    }

    @Test
    void isNotValidNegative() {
        final var record = new Record(1L,
                                      "NL42FOOB0123456789",
                                      BigDecimal.ZERO,
                                      BigDecimal.valueOf(-10),
                                      "Test",
                                      BigDecimal.TEN);
        assertFalse(record.isValid());
    }

    @Test
    void isValidZero() {
        final var record = new Record(1L,
                                      "NL42FOOB0123456789",
                                      BigDecimal.valueOf(42),
                                      BigDecimal.valueOf(-42),
                                      "Test",
                                      BigDecimal.ZERO);
        assertTrue(record.isValid());
    }
}
