package nl.ordina.rabobankcustomerstatement.model;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class Record {
    Long transactionReference;
    String iban;
    BigDecimal startBalance;
    BigDecimal mutation;
    String description;
    BigDecimal endBalance;

    public boolean isValid() {
        return startBalance.add(mutation)
                           .equals(endBalance);
    }

}
