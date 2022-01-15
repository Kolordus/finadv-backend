package pl.kolak.finansjera.financeEntity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kolak.finansjera.utils.InvalidDataException;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class FinanceDataServiceTest {

    @Mock
    FinanceEntryRepository financeEntryRepository;

    @InjectMocks
    FinanceDataService financeDataService;

    @Test
    void shouldPassValidation() {
        //given
        FinanceEntry financeEntry = new FinanceEntry("pau", LocalDate.now().toString(), "operation name", 500);

        // when
        financeDataService.validateData(financeEntry);

        //then -> should pass
    }

    @Test
    void shouldNotPassBecauseOfEmptyPersonName() {
        //given
        FinanceEntry nullPersonName = new FinanceEntry(null, LocalDate.now().toString(), "operation name", 500);
        FinanceEntry emptyPersonName = new FinanceEntry("", LocalDate.now().toString(), "operation name", 500);

        // when then
        Assertions.assertThrows(InvalidDataException.class,
                () -> financeDataService.validateData(nullPersonName));

        Assertions.assertThrows(InvalidDataException.class,
                () -> financeDataService.validateData(emptyPersonName));
    }

    @Test
    void shouldNotPassBecauseOfEmptyOperationName() {
        //given
        FinanceEntry emptyOperationName = new FinanceEntry("personName", LocalDate.now().toString(), "", 500);

        // when then
        Assertions.assertThrows(InvalidDataException.class,
                () -> financeDataService.validateData(emptyOperationName));
    }

    @Test
    void shouldNotPassBecauseOfIncorrectAmount() {
        //given
        FinanceEntry negativeAmount = new FinanceEntry("personName", LocalDate.now().toString(), "operation name", -5);
        FinanceEntry zeroAmount = new FinanceEntry("personName", LocalDate.now().toString(), "operation name", 0);

        // when then
        Assertions.assertThrows(InvalidDataException.class,
                () -> financeDataService.validateData(negativeAmount));

        Assertions.assertThrows(InvalidDataException.class,
                () -> financeDataService.validateData(zeroAmount));
    }

}