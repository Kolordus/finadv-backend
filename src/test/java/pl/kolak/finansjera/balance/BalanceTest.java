//package pl.kolak.finansjera.balance;
//
//import org.apache.tomcat.jni.Local;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import pl.kolak.finansjera.financeEntity.FinanceEntry;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class BalanceTest {
//
//    Balance balance;
//    FinanceEntry financeEntry;
//
//    @Test
//    @DisplayName("Should properly subtract from balance and who leads - add to zero")
//    void shouldHandleWhoLeads() {
//        // Given
//        balance = new Balance(0, "pau", LocalDate.now());
//        financeEntry = new FinanceEntry("jack", LocalDate.now().toString(), "operation1", 1000);
//
//        // When
//        balance.subtractAndHandleIfBalanceUnderZero(financeEntry);
//
//        // Then
//        assertEquals(balance.getBalance(), 1000);
//        assertEquals(balance.getWhoLeads(), "jack");
//    }
//
//    @Test
//    @DisplayName("Should properly subtract balance and who leads - change of leader")
//    void shouldHandleWhoLeads1() {
//        // Given
//        balance = new Balance(1000, "pau", LocalDate.now());
//        financeEntry = new FinanceEntry("jack", LocalDate.now().toString(), "operation1", 1001);
//
//        // When
//        balance.subtractAndHandleIfBalanceUnderZero(financeEntry);
//
//        // Then
//        assertEquals(balance.getBalance(), 1);
//        assertEquals(balance.getWhoLeads(), "jack");
//    }
//
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
//}