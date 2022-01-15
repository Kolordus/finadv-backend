package pl.kolak.finansjera.balance;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.kolak.finansjera.financeEntity.FinanceEntry;

import java.util.Optional;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public Balance getNewestBalance() {
        Optional<Balance> newestBalance = balanceRepository.findAll().stream()
                .min((o1, o2) -> o2.getDate().compareTo(o1.getDate()));

        return newestBalance
                .orElse(Balance.EMPTY);
    }

    public void calculateAndSaveBalance(FinanceEntry newestEntry) {
        Balance balance = getNewestBalance();

        if (balance.equals(Balance.EMPTY)) {
            balanceRepository.save(Balance.createFirstBalance(newestEntry));
            return ;
        }

        if (leadingPersonMadeAnotherExpenditure(newestEntry, balance))
            balance.addToBalance(newestEntry.getAmount());
        else
            balance.subtractAndHandleIfBalanceUnderZero(newestEntry);

        balanceRepository.save(balance);
    }

    public void subtractAndSaveBalance(FinanceEntry newestEntry) {
        Balance balance = getNewestBalance();
        balance.subtractAndHandleIfBalanceUnderZero(newestEntry);

        balanceRepository.save(balance);
    }

    public void clearBalances() {
        balanceRepository.deleteAll();
    }

    public Balance cleanEntriesAndGetLastBalance() {
        Balance newestBalance = getNewestBalance();
        this.clearBalances();
        return newestBalance;
    }
    
    private boolean leadingPersonMadeAnotherExpenditure(FinanceEntry newestEntry, Balance balance) {
        return StringUtils.equals(balance.getWhoLeads(), newestEntry.getPersonName());
    }
}
