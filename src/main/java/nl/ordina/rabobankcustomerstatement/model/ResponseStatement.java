package nl.ordina.rabobankcustomerstatement.model;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ResponseStatement {
    private final List<ErrorRecord> errorRecords;
    private Result result;

    public ResponseStatement() {
        result = Result.SUCCESSFUL;
        errorRecords = new ArrayList<>();
    }

    public void result(final Result result) {
        if (this.result == Result.INCORRECT_END_BALANCE && result == Result.DUPLICATE_REFERENCE) {
            this.result = Result.DUPLICATE_REFERENCE_INCORRECT_END_BALANCE;
        } else {
            this.result = result;
        }
    }

    public void add(final ErrorRecord errorRecord) {
        this.errorRecords.add(errorRecord);
    }
}
