package pl.kolak.finansjera.financeEntity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class FinanceEntry {

    @Id
    private String date;
    private String personName;
    private String operationName;
    private int amount;

    public FinanceEntry() { //hibernate
    }

    public FinanceEntry(String personName, String date, String name, int amount) {
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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