package pl.kolak.finansjera.balance;

import org.springframework.stereotype.Service;
import pl.kolak.finansjera.finance_entity.FinanceEntry;

import java.time.LocalDateTime;
import java.util.List;

import static pl.kolak.finansjera.utils.FinanceAppStrings.JACK;
import static pl.kolak.finansjera.utils.FinanceAppStrings.PAU;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public Balance getNewestBalance() {
        List<Balance> balances = balanceRepository.findAll();
        return balances.size() == 0 ? Balance.EMPTY : balances.get(balances.size() - 1);
    }

    public void clearBalances() {
        balanceRepository.deleteAll();
    }

    public Balance cleanEntriesAndGetLastBalance() {
        this.clearBalances();
        return getNewestBalance();
    }

    public void setNewestBalance(Balance balance) {
        balanceRepository.save(balance);
    }

    public void recalculateAndClearBalances(List<FinanceEntry> allFinanceEntries) {
        int sumForPau = getSumForPerson(PAU, allFinanceEntries);
        int sumForJack = getSumForPerson(JACK, allFinanceEntries);

        int amountBalance = sumForPau - sumForJack;

        Balance balanceToSave = Balance.EMPTY;

        if (amountBalance > 0) balanceToSave = new Balance(amountBalance, PAU, LocalDateTime.now());
        if (amountBalance < 0) balanceToSave = new Balance(Math.abs(amountBalance), JACK, LocalDateTime.now());

        setNewestBalance(balanceToSave);
    }

    private int getSumForPerson(String personName, List<FinanceEntry> allFinanceEntries) {
        return allFinanceEntries.stream()
                .filter(entry -> entry.getPersonName().equals(personName))
                .mapToInt(fe -> fe.getAmount().intValue())
                .sum();
    }

    public void entitiesToTheBalance() {
        Balance newestBalance = getNewestBalance();
        clearBalances();
        setNewestBalance(newestBalance);
    }
}
