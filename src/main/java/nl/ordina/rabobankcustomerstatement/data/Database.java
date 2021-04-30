package nl.ordina.rabobankcustomerstatement.data;

import lombok.Getter;
import nl.ordina.rabobankcustomerstatement.exception.DuplicateKeyException;
import nl.ordina.rabobankcustomerstatement.model.ErrorRecord;
import nl.ordina.rabobankcustomerstatement.model.Record;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class Database {
    @Getter
    private final List<Record> data;

    public Database() {
        data = new LinkedList<>();
    }

    public void persist(final Record record) throws DuplicateKeyException {
        if (data.stream()
                .map(Record::getTransactionReference)
                .anyMatch(aLong -> aLong.equals(record.getTransactionReference()))) {
            throw new DuplicateKeyException(new ErrorRecord(record.getTransactionReference(), record.getIban()));
        }
        this.data.add(record);
    }
}
