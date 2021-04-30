package nl.ordina.rabobankcustomerstatement.service;

import nl.ordina.rabobankcustomerstatement.data.Database;
import nl.ordina.rabobankcustomerstatement.exception.DuplicateKeyException;
import nl.ordina.rabobankcustomerstatement.model.Record;
import nl.ordina.rabobankcustomerstatement.model.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerStatementServiceTest {

    private Database data;
    private CustomerStatementService service;

    @BeforeEach
    void setUp() {
        this.data = new Database();
        this.service = new CustomerStatementService(this.data);
    }

    @Test
    void getData() throws DuplicateKeyException {
        final Record record = new Record(1L,
                                         "NL42FOOB0123456789",
                                         BigDecimal.ZERO,
                                         BigDecimal.TEN,
                                         "Test",
                                         BigDecimal.TEN);
        data.persist(record);
        final List<Record> data = this.service.data();
        assertThat(data).contains(record);
    }

    @Test
    void statement() {
        final Record record = new Record(2L,
                                         "NL42FOOB0123456789",
                                         BigDecimal.ZERO,
                                         BigDecimal.TEN,
                                         "Test",
                                         BigDecimal.TEN);
        this.service.statement(record);
        assertThat(this.data.getData()).contains(record);
    }

    @Test
    void statementDuplicate() {
        final Record record = new Record(2L,
                                         "NL42FOOB0123456789",
                                         BigDecimal.ZERO,
                                         BigDecimal.TEN,
                                         "Test",
                                         BigDecimal.TEN);
        final var answer1 = this.service.statement(record); //first will go as planned
        final var answer2 = this.service.statement(record); //will fail

        assertThat(this.data.getData()).contains(record);
        assertThat(answer1.getResult()).isEqualTo(Result.SUCCESSFUL);
        assertThat(answer1.getErrorRecords()
                          .size()).isZero();
        assertThat(answer2.getResult()).isEqualTo(Result.DUPLICATE_REFERENCE);
        assertThat(answer2.getErrorRecords()
                          .size()).isNotZero();
    }

    @Test
    void statementDuplicateAndBalance() {
        final Record record = new Record(2L,
                                         "NL42FOOB0123456789",
                                         BigDecimal.ZERO,
                                         BigDecimal.TEN,
                                         "Wrong endbalance",
                                         BigDecimal.valueOf(42));
        final var answer1 = this.service.statement(record);
        final var answer2 = this.service.statement(record);

        assertThat(this.data.getData()).contains(record);
        assertThat(answer1.getResult()).isEqualTo(Result.INCORRECT_END_BALANCE);
        assertThat(answer1.getErrorRecords()
                          .size()).isEqualTo(1);
        assertThat(answer2.getResult()).isEqualTo(Result.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE);
        assertThat(answer2.getErrorRecords()
                          .size()).isEqualTo(2);
    }
}
