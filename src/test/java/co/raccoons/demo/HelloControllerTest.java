package co.raccoons.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest extends MocMvcAware {

    @Test
    public void clearsAllMessages() throws Exception {
        perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Sample message")
        );

        perform(
                delete("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void returnsGreetingJson() throws Exception {
        perform(
                get("/api/hello")
        )
                .andExpect(status().isOk())
                .andExpect(content().json("{id:7, content:\"Eleven\"}}"));
    }

    @Test
    public void testGetMessages() throws Exception {
        perform(
                get("/api/messages")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testAddMessage() throws Exception {
        perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Test Message")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Message added: Test Message"));
    }

    @Test
    public void testUpdateMessage() throws Exception {
        perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Initial Message")
        );

        perform(
                put("/api/messages/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Updated Message")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Message updated at index 0: Updated Message"));
    }

    @Test
    public void testDeleteMessage() throws Exception {
        perform(
                post("/api/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("Message to Delete")
        );

        perform(
                delete("/api/messages/0")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("Message removed at index 0: Message to Delete"));
    }
}