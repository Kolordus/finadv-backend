package pl.kolak.finansjera.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kolak.finansjera.balance.BalanceService;
import pl.kolak.finansjera.finance_entity.FinanceDataService;
import pl.kolak.finansjera.finance_entity.FinanceEntry;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/finance")
public class FinanceController {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceController.class);
    private final FinanceDataService financeDataService;
    private final BalanceService balanceService;

    @Autowired
    public FinanceController(FinanceDataService financeDataService, BalanceService balanceService) {
        this.financeDataService = financeDataService;
        this.balanceService = balanceService;
    }

    @GetMapping
    public ResponseEntity<List<FinanceEntry>> getAllFinanceEntries(HttpServletRequest request) {
        List<FinanceEntry> allFinanceEntries = financeDataService.getAllFinanceEntries();

        LOG.info("From: {} || returned entries amount: {}", request.getRemoteAddr(), allFinanceEntries.size());

        return new ResponseEntity<>(allFinanceEntries, HttpStatus.OK);
    }

    @GetMapping("/{personName}")
    public ResponseEntity<List<FinanceEntry>> getAllDataByPersonName(@PathVariable String personName, HttpServletRequest request) {

        LOG.info("From: {} || returned entries for: {}", request.getRemoteAddr(), personName);
        
        return new ResponseEntity<>(financeDataService.readAllFinanceEntriesByName(personName.toLowerCase()),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveFinanceEntity(
            @Valid @RequestBody FinanceEntry newestEntry,
            HttpServletRequest request) {
        financeDataService.createFinanceEntry(newestEntry);
        balanceService.recalculateAndClearBalances(financeDataService.getAllFinanceEntries());

        LOG.info("From: {} || saved entity: {}", request.getRemoteAddr(), newestEntry);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> editFinanceEntity(
            @Valid @RequestBody FinanceEntry updatedEntry,
            HttpServletRequest request) {
        financeDataService.updateEntryOrThrow(updatedEntry);
        balanceService.recalculateAndClearBalances(financeDataService.getAllFinanceEntries());

        LOG.info("From: {} || entry after update: {}", request.getRemoteAddr(), updatedEntry);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllEntriesAndGetNewestBalance(HttpServletRequest request) {
        financeDataService.clearEntries();
        balanceService.entitiesToTheBalance();

        LOG.info("From: {} || performed CLEAR ENTRIES and get BALANCE", request.getRemoteAddr());
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/entry")
    public ResponseEntity<?> deleteEntry(
            @Valid @RequestBody FinanceEntry entryToDelete,
            HttpServletRequest request) {
        return financeDataService.findEntry(entryToDelete)
                .map(entry -> {
                    financeDataService.deleteEntry(entryToDelete);
                    balanceService.recalculateAndClearBalances(financeDataService.getAllFinanceEntries());
                    
                    LOG.info("From: {} || deleted entry {}", request.getRemoteAddr(), entryToDelete);

                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>("No such finance entry", HttpStatus.NOT_FOUND));
    }
}
