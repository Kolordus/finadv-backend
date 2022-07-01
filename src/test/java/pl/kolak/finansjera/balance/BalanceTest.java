package pl.kolak.finansjera.balance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.kolak.finansjera.finance_entity.FinanceEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceTest {

    Balance balance;
    FinanceEntry financeEntry;

    @Test
    @DisplayName("Should properly subtract from balance and who leads - add to zero")
    void shouldHandleWhoLeads() {
        // Given
        balance = new Balance(0, "pau", LocalDateTime.now());
        financeEntry = new FinanceEntry("jack", LocalDate.now().toString(), "operation1", new BigDecimal(1000));

        // When
//        balance.subtractAndHandleIfBalanceUnderZero(financeEntry);

        // Then
        assertEquals(balance.getBalance(), 1000);
        assertEquals(balance.getWhoLeads(), "jack");
    }

    @Test
    @DisplayName("Should properly subtract balance and who leads - change of leader")
    void shouldHandleWhoLeads1() {
        // Given
        balance = new Balance(1000, "pau", LocalDateTime.now());
        financeEntry = new FinanceEntry("jack", LocalDate.now().toString(), "operation1", new BigDecimal(1001));

        // When

        // Then
        assertEquals(balance.getBalance(), 1);
        assertEquals(balance.getWhoLeads(), "jack");
    }

//    @Test
//    @DisplayName("Should properly handle reversion")
//    void shouldHandleWEntryRevert() {
//        // Given
//        LocalDate now = LocalDate.now();
//
//        Balance firstBalance = Balance.createFirstBalance(new FinanceEntry("jack", now.toString(), "operation name", 1500));
//        financeEntry = new FinanceEntry("pau", LocalDate.now().toString(), "operation1", 2000);
//        Balance balanceAfter = new Balance(500, "pau", now);
//
//        // When
//        balanceAfter.revertLastEntry(financeEntry);
//
//        // Then
//        assertEquals(firstBalance, balanceAfter);
//    }
}