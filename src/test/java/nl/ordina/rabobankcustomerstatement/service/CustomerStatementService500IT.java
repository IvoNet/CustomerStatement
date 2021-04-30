package nl.ordina.rabobankcustomerstatement.service;

import nl.ordina.rabobankcustomerstatement.model.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static nl.ordina.TestUtil.readTestResourceAsString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerStatementService500IT {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private CustomerStatementService customerStatementServiceMock;


    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                                      .build();
    }

    @Test
    public void getMethodInternalServerError() throws Exception {
        when(customerStatementServiceMock.data()).thenThrow(IllegalStateException.class);
        mockMvc.perform(get("/api/get").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is5xxServerError())
               .andExpect(jsonPath("$.result").value("INTERNAL_SERVER_ERROR"))
               .andExpect(jsonPath("$.errorRecords").isEmpty());
    }

    @Test
    public void postMethodInternalServerError() throws Exception {
        when(customerStatementServiceMock.statement(isA(Record.class))).thenThrow(IllegalStateException.class);
        final String json = readTestResourceAsString("postMethod.json");
        mockMvc.perform(post("/api/statement")
                              .contentType(MediaType.APPLICATION_JSON)
                              .content(json))
               .andExpect(status().is5xxServerError())
               .andExpect(jsonPath("$.result").value("INTERNAL_SERVER_ERROR"))
               .andExpect(jsonPath("$.errorRecords").isEmpty());

    }
}
