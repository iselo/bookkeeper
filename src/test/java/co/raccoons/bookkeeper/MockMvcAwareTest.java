package co.raccoons.bookkeeper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

public abstract class MockMvcAwareTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
    }

    protected ResultActions perform(RequestBuilder operationToTest) throws Exception {
        return mockMvc.perform(operationToTest);
    }

    public static <T> String toJson(T instance) {
        try {
            return new ObjectMapper().writeValueAsString(instance);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize Json", e);
        }
    }
}
