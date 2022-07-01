package pl.kolak.finansjera.utils;

import pl.kolak.finansjera.finance_entity.FinanceEntry;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestUtils {

    private TestUtils() {
    }

    public static FinanceEntry DUMMY_FINANCE_ENTRY = new FinanceEntry("pau",
            LocalDateTime.now().toString(),
            "Operation 1",
            new BigDecimal(3000)
    );


}
