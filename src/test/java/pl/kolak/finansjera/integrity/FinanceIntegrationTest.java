package pl.kolak.finansjera.integrity;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pl.kolak.finansjera.balance.BalanceService;
import pl.kolak.finansjera.finance_entity.FinanceDataService;
import pl.kolak.finansjera.finance_entity.FinanceEntry;
import pl.kolak.finansjera.web.FinanceController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class FinanceIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    MockMvc mockMvc;
    @Autowired
    FinanceDataService financeDataService;
    @Autowired
    BalanceService balanceService;
    @Mock
    HttpServletRequest mockedRequest;

    @BeforeEach
    void before() {
        mongoDBContainer.start();
        initDB();
    }

    @Test
    public void method() throws Exception {
        // !!!
        MvcResult mvcResult = mockMvc.perform(get("/finance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        when(mockedRequest.getRemoteAddr())
                .thenReturn("66");

        System.out.println(mvcResult.getResponse().getStatus());

//        ResponseEntity<List<FinanceEntry>> allFinanceEntries = financeController.getAllFinanceEntries(mockedRequest);
//
//        System.out.println(allFinanceEntries);
    }


    private void initDB() {
        financeDataService.createFinanceEntry(new FinanceEntry("dupsko", "1", "d44dd", 5));
        financeDataService.createFinanceEntry(new FinanceEntry("dupskko", "2", "dd55d", 5));
        financeDataService.createFinanceEntry(new FinanceEntry("dupskllo", "3", "dd55d", 5));
    }
}
