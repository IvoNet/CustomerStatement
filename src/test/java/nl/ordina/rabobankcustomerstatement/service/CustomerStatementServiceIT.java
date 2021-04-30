package nl.ordina.rabobankcustomerstatement.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static nl.ordina.TestUtil.readTestResourceAsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerStatementServiceIT {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                                      .build();
    }

    @Test
    public void getMethod() throws Exception {
        mockMvc.perform(get("/api/get").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    public void postMethod() throws Exception {
        final String inJson = readTestResourceAsString("postMethod.json");
        mockMvc.perform(post("/api/statement")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(inJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value("SUCCESSFUL"))
               .andExpect(jsonPath("$.errorRecords").isEmpty());
    }

    @Test
    public void badRequest() throws Exception {
        mockMvc.perform(post("/api/statement")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content("{}"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.result").value("BAD_REQUEST"))
               .andExpect(jsonPath("$.errorRecords").isEmpty());
    }

    @Test
    public void postMethodWithDuplicate() throws Exception {
        final String json = readTestResourceAsString("postMethodWithDuplicate.json");
        mockMvc.perform(post("/api/statement")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value("SUCCESSFUL"))
               .andExpect(jsonPath("$.errorRecords").isEmpty());

        //duplicate
        mockMvc.perform(post("/api/statement")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value("DUPLICATE_REFERENCE"))
               .andExpect(jsonPath("$.errorRecords[0].reference").value(2));

    }

    @Test
    public void postMethodWithWrongEndBalanceAndDuplicate() throws Exception {
        final String json = readTestResourceAsString("postMethodWithWrongEndbalance.json");
        mockMvc.perform(post("/api/statement")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value("INCORRECT_END_BALANCE"))
               .andExpect(jsonPath("$.errorRecords").isNotEmpty())
               .andExpect(jsonPath("$.errorRecords[0].reference").value(3))
               .andExpect(jsonPath("$.errorRecords[0].accountNumber").value("NL62FOOB0123456789"));


        //duplicate
        mockMvc.perform(post("/api/statement")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.result").value("DUPLICATE_REFERENCE_INCORRECT_END_BALANCE"))
               .andExpect(jsonPath("$.errorRecords").isNotEmpty())
               .andExpect(jsonPath("$.errorRecords[0].accountNumber").value("NL62FOOB0123456789"))
               .andExpect(jsonPath("$.errorRecords[0].reference").value(3))
               .andExpect(jsonPath("$.errorRecords[1].accountNumber").value("NL62FOOB0123456789"))
               .andExpect(jsonPath("$.errorRecords[1].reference").value(3));
    }

}
