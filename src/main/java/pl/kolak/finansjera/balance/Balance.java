package pl.kolak.finansjera.balance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

@Document
public class Balance {

    public static final Balance EMPTY = new Balance(0, "No one", LocalDateTime.now());

    
    Comparator<Balance> COMP = Comparator.comparingInt(value -> value.balance);
    @Id
    private String id;

    private int balance;
    private String whoLeads;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SS")
    private LocalDateTime date;

    public Balance() { //hibernate
    }

    // defensive copy to assure that new balance is saved, not replaced
    public static Balance newBalance(Balance balance) {
        return new Balance(balance.balance, balance.whoLeads, balance.date);
    }

    public Balance(int balance, String whoLeads, LocalDateTime date) {
        this.balance = balance;
        this.whoLeads = whoLeads;
        this.date = date;
    }

    public void addToBalance(int amount) {
        this.balance += amount;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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
}
