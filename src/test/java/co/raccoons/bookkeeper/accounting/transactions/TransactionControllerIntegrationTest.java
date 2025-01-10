package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.MockMvcAwareTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.sql.Date;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerIntegrationTest extends MockMvcAwareTest {

    @LocalServerPort
    private Integer randomPort;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("finds all transactions")
    void findsAllTransaction() throws Exception {
        webTestClient.get()
                .uri("/transactions")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.size()").isEqualTo(2);
    }

    @Test
    @DisplayName("finds transaction by id")
    void findsByIdAndReturnsHttp200() throws Exception {
        webTestClient.get()
                .uri("/transactions/5")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(5)
                .jsonPath("$.occurredOn").isEqualTo("2025-01-01")
                .jsonPath("$.description").isEqualTo("Internet-LTE")
                .jsonPath("$.account").isEqualTo(100)
                .jsonPath("$.type").isEqualTo("DEPOSIT")
                .jsonPath("$.category").isEqualTo(500)
                .jsonPath("$.amount").isEqualTo(0.99);
    }


    @Test
    @DisplayName("findById throws transaction not found exception")
    void findByIdThrowsExceptionAndReturnsHttp404() throws Exception {
        webTestClient.get()
                .uri("/transactions/0")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(result ->
                        assertThat(getResolvedException(result))
                                .isInstanceOf(TransactionNotFoundException.class)
                )
                .jsonPath("$.message").isEqualTo("Transaction with id 0 not found");
    }

    @Test
    @DisplayName("creates transaction")
    void createsTransactionAndReturnsHttp201() throws Exception {
        var transaction = Transaction.builder()
                .id(1)
                .description("Internet-GPON")
                .occurredOn(Date.valueOf("2025-01-01"))
                .account(100)
                .type(TransactionType.DEPOSIT)
                .category(500)
                .amount(BigDecimal.valueOf(0.99))
                .build();
        webTestClient.post()
                .uri("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(transaction)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.occurredOn").isEqualTo("2025-01-01")
                .jsonPath("$.description").isEqualTo("Internet-GPON")
                .jsonPath("$.account").isEqualTo(100)
                .jsonPath("$.type").isEqualTo("DEPOSIT")
                .jsonPath("$.category").isEqualTo(500)
                .jsonPath("$.amount").isEqualTo(0.99);
    }

    @Test
    @DisplayName("creation throws exception")
    void throwsOptimisticLockExceptionOnCreate() throws Exception {
        var transaction = Transaction.builder()
                .id(1)
                .occurredOn(Date.valueOf("2025-01-01"))
                .description("Some description")
                .account(104)
                .type(TransactionType.WITHDRAWAL)
                .amount(BigDecimal.valueOf(0.99))
                .category(500)
                .version(10L)
                .build();
        webTestClient.post()
                .uri("/transactions")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(transaction)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(result ->
                        assertThat(getResolvedException(result))
                                .isInstanceOf(TransactionOptimisticLockException.class)
                )
                .jsonPath("$.message").isEqualTo("Transaction can't be created");
    }

    @Test
    @DisplayName("updates transaction by id")
    void updatesTransactionAndReturnsHttp204() throws Exception {
        Transaction transaction = Transaction.builder()
                .id(1)
                .occurredOn(Date.valueOf("2025-01-01"))
                .description("New")
                .account(101)
                .type(TransactionType.WITHDRAWAL)
                .amount(BigDecimal.valueOf(0.99))
                .category(501)
                .version(0L)
                .build();
        webTestClient.put()
                .uri("/transactions/1")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(transaction)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.occurredOn").isEqualTo("2025-01-01")
                .jsonPath("$.description").isEqualTo("New")
                .jsonPath("$.account").isEqualTo(101)
                .jsonPath("$.type").isEqualTo("WITHDRAWAL")
                .jsonPath("$.category").isEqualTo(501)
                .jsonPath("$.amount").isEqualTo(0.99);
    }

    @Test
    @DisplayName("update throws exception")
    void throwsOptimisticLockExceptionOnUpdate() throws Exception {
        Transaction transaction = Transaction.builder()
                .id(5)
                .occurredOn(Date.valueOf("2025-01-01"))
                .description("Some description")
                .account(103)
                .type(TransactionType.WITHDRAWAL)
                .amount(BigDecimal.valueOf(0.99))
                .category(500)
                .version(0L)
                .build();
        webTestClient.put()
                .uri("/transactions/5")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(transaction)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(result ->
                        assertThat(getResolvedException(result))
                                .isInstanceOf(TransactionOptimisticLockException.class)
                )
                .jsonPath("$.message").isEqualTo("Transaction with id 5 can't be updated");
    }

    @Test
    @DisplayName("deletes transaction by id")
    void deletesTransactionAndReturnsHttp204() throws Exception {
        webTestClient.delete()
                .uri("/transactions/5")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Successfully deleted");

        webTestClient.get()
                .uri("/transactions/5")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("delete throws transaction not found exception")
    void deleteThrowsExceptionAndReturnsHttp404() throws Exception {
        webTestClient.delete()
                .uri("/transactions/9")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(result ->
                        assertThat(getResolvedException(result))
                                .isInstanceOf(TransactionNotFoundException.class)
                )
                .jsonPath("$.message").isEqualTo("Transaction with id 9 not found");
    }
}
