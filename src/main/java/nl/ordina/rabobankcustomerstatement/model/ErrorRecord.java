package nl.ordina.rabobankcustomerstatement.model;

import lombok.Value;

@Value
public class ErrorRecord {
    Long reference;
    String accountNumber;
}
