package pl.kolak.finansjera.integrity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kolak.finansjera.finance_entity.FinanceEntry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static pl.kolak.finansjera.utils.TestUtils.DUMMY_FINANCE_ENTRY;

@SpringBootTest
@AutoConfigureMockMvc
public class FinanceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldReturn200() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get("/finance"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        FinanceEntry[] financeEntries =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), FinanceEntry[].class);

        // then
//        JsonPath.parse(contentAsString).read("$.[0].date");

        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(9, financeEntries.length);
    }

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldPassOnCorrectPost() throws Exception {
        ObjectWriter objWriter = objectMapper.writer().withDefaultPrettyPrinter();

        MvcResult mvcResult = mockMvc.perform(post("/finance")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objWriter.writeValueAsString(DUMMY_FINANCE_ENTRY)))
                .andReturn();


        System.out.println("aaaaaaaaaaaaaa");
        System.out.println(mvcResult.getResponse().getContentAsString());

//        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }

}
