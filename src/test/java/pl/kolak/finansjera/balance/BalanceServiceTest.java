package pl.kolak.finansjera.balance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    BalanceRepository balanceRepository;

    @InjectMocks
    BalanceService balanceService;
    
    @Test
    public void shouldCalculateAndPersistBalance() {
        when(balanceRepository.findAll())
                .thenReturn(dummyBalanceList());

//        Optional<Balance> newestBalance = balanceService.getNewestBalance();
//        
//
//        assertBalanceCorrectness(newestBalance.get());
    }

    private static void assertBalanceCorrectness(Balance balance) {
        Assertions.assertEquals(700, balance.getBalance());
        Assertions.assertEquals(LocalDate.now(), balance.getDate().toLocalDate());
        Assertions.assertEquals("jack", balance.getWhoLeads());
    }


    private List<Balance> dummyBalanceList() {
        Balance balance = new Balance(400, "pau", LocalDateTime.now().minusDays(5));
        Balance balance1 = new Balance(200, "jack", LocalDateTime.now().minusDays(1));
        Balance balance2 = new Balance(100, "pau", LocalDateTime.now().minusDays(2));
        Balance balance3 = new Balance(700, "jack", LocalDateTime.now());

        return List.of(
                balance,
                balance1,
                balance2,
                balance3
        );
    }

}