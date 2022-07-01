package pl.kolak.finansjera.integrity;


import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import pl.kolak.finansjera.balance.Balance;
import pl.kolak.finansjera.balance.BalanceService;
import pl.kolak.finansjera.finance_entity.FinanceEntry;
import pl.kolak.finansjera.web.FinanceController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
    FinanceController financeController;

    @Autowired
    BalanceService balanceService;

    @Mock
    HttpServletRequest mockedRequest;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void before() {
        mongoDBContainer.start();
        initDB();
    }

    @Test
    @DisplayName("Get entries by name")
    public void method1() throws Exception {
        // given
        String jack = "jack";
        String pau = "pau";

        // when
        MvcResult jacResult = mockMvc.perform(get("/finance/" + jack))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        FinanceEntry[] jackEntities = mapper.readValue(jacResult.getResponse().getContentAsString(), FinanceEntry[].class);

        MvcResult pauResult = mockMvc.perform(get("/finance/" + pau))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        FinanceEntry[] pauEntities = mapper.readValue(pauResult.getResponse().getContentAsString(), FinanceEntry[].class);

        // then
        assertEquals(1, jackEntities.length);
        for (FinanceEntry jackEntity : jackEntities) {
            assertEquals("jack", jackEntity.getPersonName());
        }

        assertEquals(2, pauEntities.length);
        for (FinanceEntry pauEntity : pauEntities) {
            assertEquals("pau", pauEntity.getPersonName());
        }

        initDB();
    }

    @Test
    @DisplayName("Get all entries, add one more and expect one more than before")
    public void test2() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get("/finance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        FinanceEntry[] financeEntries = mapper.readValue(mvcResult.getResponse().getContentAsString(), FinanceEntry[].class);

        // then
        assertEquals(3, financeEntries.length);

        MvcResult balanceMvcResult = mockMvc.perform(get("/balance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        String jsonResponse = balanceMvcResult.getResponse().getContentAsString();
        String whoLeads = JsonPath.parse(jsonResponse).read("$['whoLeads']");
        int amount = JsonPath.parse(jsonResponse).read("$['balance']");

        assertEquals("pau", whoLeads);
        assertEquals(5, amount);

        // // //

        // given
        FinanceEntry postContent = new FinanceEntry("pau", "5", "dd55d", new BigDecimal(5));
        mockMvc.perform(post("/finance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postContent))
                )
                .andExpect(MockMvcResultMatchers.status().is(201));

        // when
        mvcResult = mockMvc.perform(get("/finance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        FinanceEntry[] financeEntries1 = mapper.readValue(mvcResult.getResponse().getContentAsString(), FinanceEntry[].class);

        // then
        balanceMvcResult = mockMvc.perform(get("/balance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        jsonResponse = balanceMvcResult.getResponse().getContentAsString();
        whoLeads = JsonPath.parse(jsonResponse).read("$['whoLeads']");
        amount = JsonPath.parse(jsonResponse).read("$['balance']");

        assertEquals("pau", whoLeads);
        assertEquals(10, amount);

        assertEquals(4, financeEntries1.length);

        this.cleanEntries(postContent);
    }

    @Test
    @DisplayName("Test for exceptions")
    public void method() throws Exception {
        // given
        FinanceEntry personNameError = new FinanceEntry("paupau", "5", "dd55d", new BigDecimal(5));
        FinanceEntry blankDateError = new FinanceEntry("pau", "", "dd55d", new BigDecimal(5));
        FinanceEntry negativeAmountError = new FinanceEntry("pau", "5", "dd55d", new BigDecimal(-5));

        // when no.1
        MvcResult mvcResult = mockMvc.perform(post("/finance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(personNameError))
                )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn();

        // then
        String jsonResponse = mvcResult.getResponse().getContentAsString();
        String parsed = JsonPath.parse(jsonResponse).read("$[0]");

        assertTrue(parsed.contains("must match"));

        //=================// no.2

        // when
        mvcResult = mockMvc.perform(post("/finance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(blankDateError))
                )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn();

        // then
        jsonResponse = mvcResult.getResponse().getContentAsString();
        parsed = JsonPath.parse(jsonResponse).read("$[0]");

        assertTrue(parsed.contains("Cannot be empty"));

        //=================// no.3

        // when
        mvcResult = mockMvc.perform(post("/finance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(negativeAmountError))
                )
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn();

        // then
        jsonResponse = mvcResult.getResponse().getContentAsString();
        parsed = JsonPath.parse(jsonResponse).read("$[0]");

        assertTrue(parsed.contains("must be more than zero"));
    }

    @Test
    @DisplayName("Test editing feature")
    public void method5() throws Exception {
        // given
        String changedOperationName = "after editing";
        BigDecimal changedAmount = new BigDecimal(5);

        FinanceEntry financeEntry = new FinanceEntry("jack", "2022-06-20", "before editing", new BigDecimal(10));
        financeController.saveFinanceEntity(financeEntry, mockedRequest);

        // when
        financeEntry.setAmount(changedAmount);
        financeEntry.setOperationName(changedOperationName);

        mockMvc.perform(put("/finance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(FinanceEntry.from(financeEntry)))
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        MvcResult mvcResult = mockMvc.perform(get("/finance"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        FinanceEntry[] financeEntries = mapper.readValue(mvcResult.getResponse().getContentAsString(), FinanceEntry[].class);
        Optional<FinanceEntry> entryOpt = Arrays.stream(financeEntries)
                .filter(entry -> entry.getOperationName().contains("after editing"))
                .findAny();

        FinanceEntry entry = new FinanceEntry();

        if (entryOpt.isPresent()) {
            entry = entryOpt.get();
        }

        MvcResult balanceMvcResult = mockMvc.perform(get("/balance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        String jsonResponse = balanceMvcResult.getResponse().getContentAsString();
        String whoLeads = JsonPath.parse(jsonResponse).read("$['whoLeads']");
        int amount = JsonPath.parse(jsonResponse).read("$['balance']");

        // then
        assertEquals("No one", whoLeads);
        assertEquals(0, amount);

        assertEquals(new BigDecimal(5), entry.getAmount());
        assertEquals("after editing", entry.getOperationName());
        assertEquals("jack", entry.getPersonName());
        
        this.cleanEntries(entry);
    }

    // todo: delete entry
    // todo: clean entries
    private void cleanEntries(FinanceEntry... entries) {
        for (FinanceEntry entry : entries) {
            financeController.deleteEntry(entry, mockedRequest);
        }
    }

    private void initDB() {
        financeController.saveFinanceEntity(new FinanceEntry("pau", "2022-06-11", "d44dd", new BigDecimal(5)), mockedRequest);
        financeController.saveFinanceEntity(new FinanceEntry("pau", "2022-06-13", "dd55d", new BigDecimal(5)), mockedRequest);
        financeController.saveFinanceEntity(new FinanceEntry("jack", "2022-06-12", "dd55d", new BigDecimal(5)), mockedRequest);
    }
}
