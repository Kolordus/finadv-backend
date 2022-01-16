package pl.kolak.finansjera.financeEntity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class FinanceEntry {

    @Id
    private String date;
    private String personName;
    private String name;
    private int amount;

    public FinanceEntry() { //hibernate
    }

    public FinanceEntry(String personName, String date, String name, int amount) {
        this.personName = personName;
        this.date = date;
        this.name = name;
        this.amount = amount;
    }

    public static FinanceEntry from(FinanceEntry entry) {
        return new FinanceEntry(entry.getPersonName(), entry.getDate(), entry.getName(), entry.getAmount());
    }

    public void updateEntryValues(FinanceEntry entry) {
        this.setAmount(entry.getAmount());
        this.setDate(entry.getDate());
        this.setName(entry.getName());
    }

    // hibernate
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinanceEntry that = (FinanceEntry) o;
        return amount == that.amount && Objects.equals(personName, that.personName) && Objects.equals(date, that.date) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personName, date, name, amount);
    }

}