package pl.kolak.finansjera.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kolak.finansjera.balance.Balance;
import pl.kolak.finansjera.balance.BalanceService;

@RestController
@RequestMapping("/balance")
class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/clean")
    public ResponseEntity<Balance> cleanBalanceAndGetLastBalance() {
        return ResponseEntity.ok(balanceService.cleanEntriesAndGetLastBalance());
    }

    @GetMapping
    public ResponseEntity<Balance> getNewestBalance() {
        return ResponseEntity.ok(balanceService.getNewestBalance());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllBalances() {
        balanceService.clearBalances();

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
