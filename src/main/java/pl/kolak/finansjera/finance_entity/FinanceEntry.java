package pl.kolak.finansjera.finance_entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Objects;

import static pl.kolak.finansjera.utils.FinanceAppStrings.*;

@Document
public class FinanceEntry {
    
    @Id
    @NotBlank(message = CANNOT_BE_EMPTY)
    private String date;

    @NotBlank(message = CANNOT_BE_EMPTY)
    @Pattern(regexp = PAU_OR_JACK_REGEX)
    private String personName;

    @NotBlank(message = CANNOT_BE_EMPTY)
    private String operationName;

    @NotNull
    @PositiveOrZero(message = MUST_BE_MORE_THAN_ZERO)
    private BigDecimal amount;

    public FinanceEntry(String personName, String date, String name, BigDecimal amount) {
        this.personName = personName;
        this.date = date;
        this.operationName = name;
        this.amount = amount;
    }

    public static FinanceEntry from(FinanceEntry entry) {
        return new FinanceEntry(entry.getPersonName(), entry.getDate(), entry.getOperationName(), entry.getAmount());
    }

    public void updateEntryValues(FinanceEntry entry) {
        this.setAmount(entry.getAmount());
        this.setDate(entry.getDate());
        this.setOperationName(entry.getOperationName());
    }

    // hibernate
    public FinanceEntry() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
    // hibernate

    @Override
    public String toString() {
        return "FinanceEntry{" +
                ", personName='" + personName + '\'' +
                ", date='" + date + '\'' +
                ", name='" + operationName + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinanceEntry that = (FinanceEntry) o;
        return amount == that.amount && Objects.equals(personName, that.personName) && Objects.equals(date, that.date) && Objects.equals(operationName, that.operationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personName, date, operationName, amount);
    }

}