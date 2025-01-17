package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.BookkeeperNotFoundException;
import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import co.raccoons.bookkeeper.BookkeeperOptimisticLockException;
import co.raccoons.bookkeeper.MockMvcAwareTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TransactionControllerTest extends MockMvcAwareTest {

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private Transaction transaction;

    @Test
    @DisplayName("finds all transactions")
    void findsAllTransaction() throws Exception {
        var transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        when(transactionService.findAll())
                .thenReturn(transactions);

        perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()").value(transactions.size()));

        verify(transactionService).findAll();
    }

    @Test
    @DisplayName("finds transaction by id")
    void findsByIdAndReturnsHttp200() throws Exception {

        when(transactionService.findById(5))
                .thenReturn(transaction);

        perform(get("/transactions/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.occurredOn").value("2025-01-01"))
                .andExpect(jsonPath("$.description").value("Internet-LTE"))
                .andExpect(jsonPath("$.account").value(100))
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.category").value(500))
                .andExpect(jsonPath("$.amount").value(0.99));

        verify(transactionService).findById(5);
    }

    @Test
    @DisplayName("findById throws transaction not found exception")
    void findByIdThrowsExceptionAndReturnsHttp404() throws Exception {
        when(transactionService.findById(0))
                .thenThrow(new BookkeeperNotFoundException("Transaction with id 0 not found"));

        perform(get("/transactions/0"))
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(BookkeeperNotFoundException.class)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        verify(transactionService).findById(0);
    }

    @Test
    @DisplayName("creates transaction")
    void createsTransactionAndReturnsHttp201() throws Exception {
        when(transactionService.create(transaction))
                .thenReturn(transaction);

        perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(transaction))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.occurredOn").value("2025-01-01"))
                .andExpect(jsonPath("$.description").value("Internet-LTE"))
                .andExpect(jsonPath("$.account").value(100))
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.category").value(500))
                .andExpect(jsonPath("$.amount").value(0.99));


        verify(transactionService).create(transaction);
    }

    @Test
    @DisplayName("creation throws exception")
    void throwsOptimisticLockExceptionOnCreate() throws Exception {
        when(transactionService.create(transaction))
                .thenThrow(new BookkeeperOptimisticLockException("Transaction can't be created"));

        perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(transaction))
        )
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(BookkeeperOptimisticLockException.class))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Transaction can't be created"));

        verify(transactionService).create(transaction);
    }

    @Test
    @DisplayName("updates transaction")
    void updatesTransactionAndReturnsHttp204() throws Exception {
        var newTransaction = Transaction.builder()
                .id(3)
                .occurredOn(LocalDate.parse("2025-01-01"))
                .description("New")
                .account(103)
                .type(TransactionType.WITHDRAWAL)
                .amount(BigDecimal.valueOf(0.99))
                .category(503)
                .build();

        when(transactionService.update(newTransaction))
                .thenReturn(newTransaction);

        perform(put("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(newTransaction))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.occurredOn").value("2025-01-01"))
                .andExpect(jsonPath("$.description").value("New"))
                .andExpect(jsonPath("$.account").value(103))
                .andExpect(jsonPath("$.type").value("WITHDRAWAL"))
                .andExpect(jsonPath("$.category").value(503))
                .andExpect(jsonPath("$.amount").value(0.99));

        verify(transactionService).update(newTransaction);
    }

    @Test
    @DisplayName("update throws exception")
    void throwsOptimisticLockExceptionOnUpdate() throws Exception {
        when(transactionService.update(transaction))
                .thenThrow(new BookkeeperOptimisticLockException("Transaction with id 5 can't be updated"));

        perform(put("/transactions")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(transaction))
        )
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(BookkeeperOptimisticLockException.class))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Transaction with id 5 can't be updated"));

        verify(transactionService).update(transaction);
    }

    @Test
    @DisplayName("deletes transaction by id")
    void deletesTransactionAndReturnsHttp204() throws Exception {
        when(transactionService.delete(5))
                .thenReturn(new BookkeeperOperationStatus("Successfully deleted"));

        perform(delete("/transactions/5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Successfully deleted"));

        verify(transactionService).delete(5);
    }

    @Test
    @DisplayName("delete throws transaction not found exception")
    void deleteThrowsExceptionAndReturnsHttp404() throws Exception {
        when(transactionService.delete(9))
                .thenThrow(new BookkeeperNotFoundException("Transaction with id 9 not found"));

        perform(delete("/transactions/9"))
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(BookkeeperNotFoundException.class)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Transaction with id 9 not found"));

        verify(transactionService).delete(9);
    }

    @Test
    @DisplayName("delete throws conflict exception")
    void deleteThrowsExceptionAndReturnsHttp409() throws Exception {
        when(transactionService.delete(9))
                .thenThrow(new BookkeeperOptimisticLockException("Transaction with id 9 can't be deleted"));

        perform(delete("/transactions/9"))
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(BookkeeperOptimisticLockException.class)
                )
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("Transaction with id 9 can't be deleted"));

        verify(transactionService).delete(9);
    }
}
