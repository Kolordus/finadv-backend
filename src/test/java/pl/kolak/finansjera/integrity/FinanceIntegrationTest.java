package pl.kolak.finansjera.integrity;


import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pl.kolak.finansjera.balance.BalanceService;
import pl.kolak.finansjera.financeEntity.FinanceDataService;
import pl.kolak.finansjera.financeEntity.FinanceEntry;
import pl.kolak.finansjera.web.FinanceController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
public class FinanceIntegrationTest {

    @Autowired
    FinanceDataService financeDataService;

    @Autowired
    BalanceService balanceService;

    @Mock
    HttpServletRequest mockedRequest;

    FinanceController financeController;

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void before() {
        mongoDBContainer.start();
        financeController = new FinanceController(financeDataService, balanceService);
        initDB();
    }

    @Test
    public void method() {

        when(mockedRequest.getRemoteAddr())
                .thenReturn("66");

        ResponseEntity<List<FinanceEntry>> allFinanceEntries = financeController.getAllFinanceEntries(mockedRequest);

        System.out.println(allFinanceEntries);
    }


    private void initDB() {
        financeDataService.saveFinanceEntry(new FinanceEntry("dupsko", "1", "d44dd", 5));
        financeDataService.saveFinanceEntry(new FinanceEntry("dupskko", "2", "dd55d", 5));
        financeDataService.saveFinanceEntry(new FinanceEntry("dupskllo", "3", "dd55d", 5));
    }
}
