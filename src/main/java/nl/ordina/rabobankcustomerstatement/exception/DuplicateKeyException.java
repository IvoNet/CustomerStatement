package nl.ordina.rabobankcustomerstatement.exception;

import lombok.Getter;
import nl.ordina.rabobankcustomerstatement.model.ErrorRecord;

public class DuplicateKeyException extends Exception {
    @Getter
    private final transient ErrorRecord errorRecord;

    public DuplicateKeyException(final ErrorRecord errorRecord) {
        this.errorRecord = errorRecord;
    }
}
