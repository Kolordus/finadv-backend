package pl.kolak.finansjera.integrity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static pl.kolak.finansjera.utils.TestUtils.DUMMY_FINANCE_ENTRY;

@SpringBootTest
@AutoConfigureMockMvc
public class FinanceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldReturn200() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/finance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();


        String contentAsString = mvcResult.getResponse().getContentAsString();
        JsonPath.parse(contentAsString).read("$.[0].date");
        System.out.println(contentAsString);
    }

    @Test
    public void shouldPassOnCorrectPost() throws Exception {

        ObjectWriter objWriter = objectMapper.writer().withDefaultPrettyPrinter();

        MvcResult mvcResult = mockMvc.perform(post("/finance")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objWriter.writeValueAsString(DUMMY_FINANCE_ENTRY)))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andReturn();


        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

}
