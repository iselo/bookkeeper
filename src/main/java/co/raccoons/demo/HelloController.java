package co.raccoons.demo;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final List<String> messages = new ArrayList<>();

    @GetMapping(path = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public Greeting sayHello() {
        return Greeting.builder()
                .id(7)
                .content("Eleven")
                .build();
    }

    @GetMapping("/messages")
    public List<String> getMessages() {
        return messages;
    }

    @PostMapping("/messages")
    public String addMessage(@RequestBody String message) {
        messages.add(message);
        return "Message added: " + message;
    }

    @PutMapping("/messages/{index}")
    public String updateMessage(@PathVariable int index, @RequestBody String updatedMessage) {
        if (index < messages.size()) {
            messages.set(index, updatedMessage);
            return "Message updated at index " + index + ": " + updatedMessage;
        } else {
            return "Invalid index";
        }
    }

    @DeleteMapping("/messages/{index}")
    public String deleteMessage(@PathVariable int index) {
        if (index < messages.size()) {
            String removedMessage = messages.remove(index);
            return "Message removed at index " + index + ": " + removedMessage;
        } else {
            return "Invalid index";
        }
    }

    @VisibleForTesting
    @DeleteMapping("/messages")
    List<String> clearMessages() {
        messages.clear();
        return messages;
    }
}
