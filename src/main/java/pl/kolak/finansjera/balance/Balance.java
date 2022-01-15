package pl.kolak.finansjera.balance;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.kolak.finansjera.financeEntity.FinanceEntry;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document
public class Balance {

    public static final Balance EMPTY = new Balance(0, "No one", LocalDate.now());

    @Id
    private String id;

    private int balance;
    private String whoLeads;
    private LocalDate date;

    public Balance() { //hibernate
    }

    public static Balance createFirstBalance(FinanceEntry entry) {
        return new Balance(entry.getAmount(), entry.getPersonName(), LocalDate.now());
    }

    public Balance(int balance, String whoLeads, LocalDate date) {
        this.balance = balance;
        this.whoLeads = whoLeads;
        this.date = date;
    }

    public void addToBalance(int amount) {
        this.balance += amount;
    }

    public void subtractAndHandleIfBalanceUnderZero(FinanceEntry entry) {
        this.subtractFromBalance(entry.getAmount());
        if (this.balance <= 0) {
            this.setWhoLeads(entry.getPersonName());
            this.balance = Math.abs(this.balance);
        }
    }

    private void subtractFromBalance(int amount) {
        this.balance -= amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getWhoLeads() {
        return whoLeads;
    }

    public void setWhoLeads(String whoLeads) {
        this.whoLeads = whoLeads;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "id='" + id + '\'' +
                ", balance=" + balance +
                ", whoLeads='" + whoLeads + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance balance1 = (Balance) o;
        return balance == balance1.balance && Objects.equals(id, balance1.id) && Objects.equals(whoLeads, balance1.whoLeads) && Objects.equals(date, balance1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, whoLeads, date);
    }

    public class PersonBalance {
        String name;
        int balance;

        public PersonBalance(String name, int balance) {
            this.name = name;
            this.balance = balance;
        }
    }
}
