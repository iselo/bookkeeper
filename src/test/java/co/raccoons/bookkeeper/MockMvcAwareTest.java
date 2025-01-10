package co.raccoons.bookkeeper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureMockMvc
public abstract class MockMvcAwareTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected ResultActions perform(RequestBuilder operationToTest) throws Exception {
        return mockMvc.perform(operationToTest);
    }

    protected  <T> String toJson(T instance) {
        try {
            return objectMapper.writeValueAsString(instance);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize Json", e);
        }
    }

    protected Exception getResolvedException(EntityExchangeResult<byte[]> entityExchangeResult) {
        return ((MvcResult) entityExchangeResult.getMockServerResult()).getResolvedException();
    }
}
