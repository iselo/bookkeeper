package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.MockMvcAwareTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
//@AutoConfigureMockMvc
@WebMvcTest(TransactionController.class)
class TransactionControllerTest extends MockMvcAwareTest {

    @MockBean
    private TransactionRepository repository;

    private final List<Transaction> transactions = new ArrayList<>();

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        this.transaction =
                Transaction.builder()
                        .id(5)
                        .description("Internet-LTE")
                        .occurredOn(Date.valueOf("2025-01-01"))
                        .account(100)
                        .type(TransactionType.DEPOSIT)
                        .category(500)
                        .amount(BigDecimal.valueOf(0.99))
                        .build();
        transactions.add(transaction);
    }

    @Test
    @DisplayName("finds all transactions")
    void findsAllTransaction() throws Exception {
        doReturn(transactions)
                .when(repository)
                .findAll();
        perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()", is(transactions.size())));
    }

    @Test
    @DisplayName("finds transaction by id")
    void findsByIdAndReturnsHttp200() throws Exception {
        doReturn(Optional.of(transaction))
                .when(repository)
                .findById(ArgumentMatchers.anyInt());
        perform(get("/transactions/5"))
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
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("creation throws exception")
    void throwsOptimisticLockExceptionOnCreate() throws Exception {
        doThrow(new OptimisticLockingFailureException("Creation error"))
                .when(repository)
                .save(any());
        perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(transaction))
        )
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
        var transaction = Transaction.builder()
                .id(3)
                .occurredOn(Date.valueOf("2025-01-01"))
                .description("New")
                .account(103)
                .type(TransactionType.WITHDRAWAL)
                .amount(BigDecimal.valueOf(0.99))
                .category(503)
                .build();
        doReturn(transaction)
                .when(repository)
                .save(any());
        perform(put("/transactions/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(transaction))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.occurredOn", is("2025-01-01")))
                .andExpect(jsonPath("$.description", is("New")))
                .andExpect(jsonPath("$.account", is(103)))
                .andExpect(jsonPath("$.type", is("WITHDRAWAL")))
                .andExpect(jsonPath("$.category", is(503)))
                .andExpect(jsonPath("$.amount", is(0.99)));
    }

    @Test
    @DisplayName("update throws exception")
    void throwsOptimisticLockExceptionOnUpdate() throws Exception {
        doThrow(new OptimisticLockingFailureException("Updated error"))
                .when(repository)
                .save(any());
        perform(put("/transactions/3")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(transaction))
        )
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(TransactionOptimisticLockException.class))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("Transaction with id 3 can't be updated")));
    }

    @Test
    @DisplayName("deletes transaction by id")
    void deletesTransactionAndReturnsHttp204() throws Exception {
        doReturn(Optional.of(transaction))
                .when(repository)
                .findById(anyInt());
        perform(delete("/transactions/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("{message:'Successfully deleted'}"));
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

    @Test
    @DisplayName("delete throws conflict exception")
    void deleteThrowsExceptionAndReturnsHttp409() throws Exception {
        doThrow(new OptimisticLockingFailureException("Delete error"))
                .when(repository)
                .findById(anyInt());
        perform(delete("/transactions/9"))
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(TransactionOptimisticLockException.class)
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", is("Transaction with id 9 can't be deleted")));
    }
}
