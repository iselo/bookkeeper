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
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .jsonPath("$.size()", is(2));

        perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    @DisplayName("finds transaction by id")
    void findsByIdAndReturnsHttp200() throws Exception {
        perform(get("/transactions/5"))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.occurredOn", is("2025-01-01")))
                .andExpect(jsonPath("$.description", is("Internet-LTE")))
                .andExpect(jsonPath("$.account", is(100)))
                .andExpect(jsonPath("$.type", is("DEPOSIT")))
                .andExpect(jsonPath("$.category", is(500)))
                .andExpect(jsonPath("$.amount", is(0.99)));
    }


    @Test
    @DisplayName("findById throws transaction not found exception")
    void findByIdThrowsExceptionAndReturnsHttp404() throws Exception {
        perform(get("/transactions/0"))
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(TransactionNotFoundException.class)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("Transaction with id 0 not found")));
    }

    @Test
    @DisplayName("creates transaction")
    void createsTransactionAndReturnsHttp201() throws Exception {
        perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        toJson(
                                Transaction.builder()
                                        .id(1)
                                        .description("Internet-GPON")
                                        .occurredOn(Date.valueOf("2025-01-01"))
                                        .account(100)
                                        .type(TransactionType.DEPOSIT)
                                        .category(500)
                                        .amount(BigDecimal.valueOf(0.99))
                                        .build()
                        )
                ))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.occurredOn", is("2025-01-01")))
                .andExpect(jsonPath("$.description", is("Internet-GPON")))
                .andExpect(jsonPath("$.account", is(100)))
                .andExpect(jsonPath("$.type", is("DEPOSIT")))
                .andExpect(jsonPath("$.category", is(500)))
                .andExpect(jsonPath("$.amount", is(0.99)));
    }

    @Test
    @DisplayName("creation throws exception")
    void throwsOptimisticLockExceptionOnCreate() throws Exception {
        perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                        toJson(
                                Transaction.builder()
                                        .id(1)
                                        .occurredOn(Date.valueOf("2025-01-01"))
                                        .description("Some description")
                                        .account(104)
                                        .type(TransactionType.WITHDRAWAL)
                                        .amount(BigDecimal.valueOf(0.99))
                                        .category(500)
                                        .version(10L)
                                        .build()
                        )
                ))
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(TransactionOptimisticLockException.class))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("Transaction can't be created")));
    }

    @Test
    @DisplayName("updates transaction by id")
    void updatesTransactionAndReturnsHttp204() throws Exception {
        perform(put("/transactions/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(
                        toJson(
                                Transaction.builder()
                                        .id(1)
                                        .occurredOn(Date.valueOf("2025-01-01"))
                                        .description("New")
                                        .account(105)
                                        .type(TransactionType.WITHDRAWAL)
                                        .amount(BigDecimal.valueOf(0.99))
                                        .category(505)
                                        .version(0L)
                                        .build()
                        )
                ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.occurredOn", is("2025-01-01")))
                .andExpect(jsonPath("$.description", is("New")))
                .andExpect(jsonPath("$.account", is(105)))
                .andExpect(jsonPath("$.type", is("WITHDRAWAL")))
                .andExpect(jsonPath("$.category", is(505)))
                .andExpect(jsonPath("$.amount", is(0.99)));
    }

    @Test
    @DisplayName("update throws exception")
    void throwsOptimisticLockExceptionOnUpdate() throws Exception {
        perform(put("/transactions/5")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(
                                Transaction.builder()
                                        .id(5)
                                        .occurredOn(Date.valueOf("2025-01-01"))
                                        .description("Some description")
                                        .account(103)
                                        .type(TransactionType.WITHDRAWAL)
                                        .amount(BigDecimal.valueOf(0.99))
                                        .category(500)
                                        .version(0L)
                                        .build()
                        )
                ))
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(TransactionOptimisticLockException.class))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("Transaction with id 5 can't be updated")));
    }


    @Test
    @DisplayName("deletes transaction by id")
    void deletesTransactionAndReturnsHttp204() throws Exception {
        perform(delete("/transactions/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("{message:'Successfully deleted'}"));

        perform(get("/transactions/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("delete throws transaction not found exception")
    void deleteThrowsExceptionAndReturnsHttp404() throws Exception {
        perform(delete("/transactions/9"))
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(TransactionNotFoundException.class)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("Transaction with id 9 not found")));
    }
}
