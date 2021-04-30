package nl.ordina.rabobankcustomerstatement.service;

import lombok.AllArgsConstructor;
import nl.ordina.rabobankcustomerstatement.data.Database;
import nl.ordina.rabobankcustomerstatement.exception.DuplicateKeyException;
import nl.ordina.rabobankcustomerstatement.model.ErrorRecord;
import nl.ordina.rabobankcustomerstatement.model.Record;
import nl.ordina.rabobankcustomerstatement.model.ResponseStatement;
import nl.ordina.rabobankcustomerstatement.model.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CustomerStatementService {

    private final Database database;

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Record> data() {
        return database.getData();
    }

    @PostMapping(path = "/statement",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseStatement statement(@RequestBody final Record record) {
        final var response = new ResponseStatement();

        if (!record.isValid()) {
            response.add(new ErrorRecord(record.getTransactionReference(), record.getIban()));
            response.result(Result.INCORRECT_END_BALANCE);
        }

        try {
            this.database.persist(record);
        } catch (final DuplicateKeyException e) {
            response.add(e.getErrorRecord());
            response.result(Result.DUPLICATE_REFERENCE);
        }

        return response;
    }

}
