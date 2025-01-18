package co.raccoons.bookkeeper.auth.signup;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.startsWith;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "PT15S")
@AutoConfigureMockMvc(addFilters = false)
public class SignupControllerEndToEndTest {

    @LocalServerPort
    @SuppressWarnings("unused")
    private Integer randomPort;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SignupRequest signupRequest;

    @Autowired
    private UserRepository repository;

    @AfterEach
    void tearDown() {
        repository.findById("test@test.com")
                .ifPresent(repository::delete);
    }

    @Test
    @DisplayName("can signup")
    void createsUserAndReturnsHttp201() {
        webTestClient.post()
                .uri("/signup")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(signupRequest)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("User created");
    }

    @Test
    @DisplayName("returns conflict status if user already exists")
    void returnsHttp409WithMessage() {
        webTestClient.post()
                .uri("/signup")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(signupRequest)
                .exchange();

        webTestClient.post()
                .uri("/signup")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(signupRequest)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("User already exists: test@test.com");
    }

    @Test
    @DisplayName("returns bad request if signup request not valid")
    void returnsHttp400OnInvalidInput() {
        var invalidSignupRequest = SignupRequest.builder().build();

        webTestClient.post()
                .uri("/signup")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(invalidSignupRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").value(startsWith("Validation failed for argument"));

    }

}
