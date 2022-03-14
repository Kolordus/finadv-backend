package pl.kolak.finansjera.integrity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockRequestDispatcher;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import pl.kolak.finansjera.stuffrequest.StuffRequest;
import pl.kolak.finansjera.stuffrequest.StuffRequestService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class StuffRequestControllerTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    static void mongoDbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    StuffRequestService stuffRequestService;

    @BeforeEach
    void before() {
        mongoDBContainer.start();
        initDB();
    }

    @Test
    void deleteByDate() throws Exception {
        String contentAsString = mockMvc.perform(get("/requests")).andReturn().getResponse().getContentAsString();
        StuffRequest[] stuffRequests = objectMapper.readValue(contentAsString, StuffRequest[].class);
        System.out.println(stuffRequests.length);

        MvcResult delete = mockMvc.perform(delete("/requests")
                        .content("2021-01-01")
                )
                .andReturn();

        String contentAsString1 = mockMvc.perform(get("/requests")).andReturn().getResponse().getContentAsString();
        StuffRequest[] stuffRequests1 = objectMapper.readValue(contentAsString1, StuffRequest[].class);
        System.out.println(stuffRequests1.length);

        MvcResult badDelete = mockMvc.perform(delete("/requests")
                        .content("202111-01111-01111")
                )
                .andReturn();

        String contentAsString2 = mockMvc.perform(get("/requests")).andReturn().getResponse().getContentAsString();
        StuffRequest[] stuffRequests2 = objectMapper.readValue(contentAsString2, StuffRequest[].class);
        System.out.println(stuffRequests2.length);

    }

    private void initDB() {
        stuffRequestService.createStuffRequest(new StuffRequest("2021-01-01", "pau", "operacja1"));
        stuffRequestService.createStuffRequest(new StuffRequest("2022-02-02", "pau", "operacja2"));
        stuffRequestService.createStuffRequest(new StuffRequest("2023-03-03", "pau", "operacja3"));
    }

}
