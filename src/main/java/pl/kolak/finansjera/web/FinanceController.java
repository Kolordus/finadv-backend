package pl.kolak.finansjera.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kolak.finansjera.balance.BalanceService;
import pl.kolak.finansjera.financeEntity.FinanceDataService;
import pl.kolak.finansjera.financeEntity.FinanceEntry;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/finance")
class FinanceController {

    private static final Logger LOG = LoggerFactory.getLogger(FinanceController.class);

    private final FinanceDataService financeDataService;
    private final BalanceService balanceService;

    public FinanceController(FinanceDataService financeDataService, BalanceService balanceService) {
        this.financeDataService = financeDataService;
        this.balanceService = balanceService;
    }

    @GetMapping
    public ResponseEntity<List<FinanceEntry>> getAllData(HttpServletRequest request) {
        List<FinanceEntry> allFinanceEntries = financeDataService.getAllFinanceEntries();

        LOG.info("From: {} ||  returned entries amount: {}", request.getRemoteAddr(), allFinanceEntries.size());
        return new ResponseEntity<>(allFinanceEntries, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> saveFinanceEntity(@RequestBody FinanceEntry newestEntry, HttpServletRequest request) {
        financeDataService.validateData(newestEntry);
        financeDataService.saveFinanceEntry(newestEntry);
        balanceService.calculateAndSaveBalance(newestEntry);

        LOG.info("From: {} || saved entity: {}", request.getRemoteAddr(), newestEntry);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> editFinanceEntity(@RequestBody FinanceEntry updatedEntry, HttpServletRequest request) {
        financeDataService.validateData(updatedEntry);

        FinanceEntry entryBeforeUpdate = financeDataService.updateFinanceEntry(updatedEntry);

        balanceService.recalculateBalance(entryBeforeUpdate, updatedEntry);

        LOG.info("From: {} || entry after update: {}", request.getRemoteAddr(), updatedEntry);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllEntriesAndGetNewestBalance() {
        financeDataService.clearEntries();

        return ResponseEntity.ok(balanceService.getNewestBalance());
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<FinanceEntry>> getAllDataByName(@PathVariable String name) {
        return new ResponseEntity<>(financeDataService.getAllFinanceEntriesByName(name.toLowerCase()),
                HttpStatus.OK);
    }

    @DeleteMapping("/entry")
    public ResponseEntity<?> deleteEntry(@RequestBody FinanceEntry entry) {
        financeDataService.deleteEntryOrThrow(entry);
        balanceService.revertLastEntry(entry);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/kurwa")
    public String method() {
        return "kurwa";
    }

}
