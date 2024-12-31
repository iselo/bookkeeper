package co.raccoons.demo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

abstract class MocMvcAware {

    @Autowired
    private MockMvc mockMvc;

    ResultActions perform(RequestBuilder operationToTest) throws Exception {
        return mockMvc.perform(operationToTest);
    }

    @BeforeEach
    public void setUp() throws Exception {
        perform(
                delete("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
