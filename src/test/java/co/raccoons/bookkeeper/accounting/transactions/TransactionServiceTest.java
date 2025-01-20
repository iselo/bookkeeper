package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import co.raccoons.bookkeeper.BookkeeperOptimisticLockException;
import co.raccoons.bookkeeper.MockMvcAwareTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TransactionServiceTest extends MockMvcAwareTest {

    @MockBean
    private TransactionRepository repository;

    private TransactionService service;

    @Autowired
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        service = new TransactionService(repository);
    }

    @Test
    @DisplayName("finds all transaction in repository")
    void serviceFindsAllTransactionInRepository() {
        var transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        when(repository.findAll())
                .thenReturn(transactions);

        assertThat(service.findAll())
                .isEqualTo(transactions);

        verify(repository).findAll();
    }

    @Test
    @DisplayName("find transaction by id in repository")
    void serviceFindsByIdInRepository() {
        when(repository.findById(5))
                .thenReturn(Optional.of(transaction));

        assertThat(service.findById(5))
                .isEqualTo(transaction);

        verify(repository).findById(5);
    }

    @Test
    @DisplayName("throws exception if not found in repository")
    void serviceThrowsExceptionIfNotFound() {
        when(repository.findById(0))
                .thenThrow(new TransactionNotFoundException("Transaction with id 0 not found"));

        assertThatThrownBy(() -> service.findById(0))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessage("Transaction with id 0 not found");

        verify(repository).findById(0);
    }

    @Test
    @DisplayName("creates transaction in repository")
    void serviceCreatesTransactionInRepository() {
        when(repository.save(transaction))
                .thenReturn(transaction);

        assertThat(service.create(transaction))
                .isEqualTo(transaction);

        verify(repository).save(transaction);
    }

    @Test
    @DisplayName("create conflict throws exception")
    void serviceThrowsExceptionIfCreateConflict() {
        when(repository.save(transaction))
                .thenThrow(new BookkeeperOptimisticLockException("Transaction can't be created"));

        assertThatThrownBy(() -> service.create(transaction))
                .isInstanceOf(BookkeeperOptimisticLockException.class)
                .hasMessage("Transaction can't be created");

        verify(repository).save(transaction);
    }


    @Test
    @DisplayName("updates transaction in repository")
    void serviceUpdatesTransactionInRepository() {
        when(repository.save(transaction))
                .thenReturn(transaction);

        assertThat(service.update(transaction))
                .isEqualTo(transaction);

        verify(repository).save(transaction);
    }

    @Test
    @DisplayName("update conflict throws exception")
    void serviceThrowsExceptionIfUpdateConflict() {
        when(repository.save(transaction))
                .thenThrow(new BookkeeperOptimisticLockException("Transaction can't be updated"));

        assertThatThrownBy(() -> service.update(transaction))
                .isInstanceOf(BookkeeperOptimisticLockException.class)
                .hasMessage("Transaction can't be updated");

        verify(repository).save(transaction);
    }

    @Test
    @DisplayName("deletes transaction in repository")
    void serviceDeletesTransactionInRepository() {
        when(repository.findById(5))
                .thenReturn(Optional.of(transaction));

        assertThat(service.delete(5))
                .isEqualTo(new BookkeeperOperationStatus("Successfully deleted"));

        verify(repository).findById(5);
        verify(repository).delete(transaction);
    }

    @Test
    @DisplayName("delete conflict throws exception")
    void serviceThrowsExceptionIfDeleteConflict() {
        when(repository.findById(5))
                .thenReturn(Optional.of(transaction));
        doThrow(new BookkeeperOptimisticLockException("Transaction with id 5 can't be deleted"))
                .when(repository)
                .delete(transaction);

        assertThatThrownBy(() -> service.delete(5))
                .isInstanceOf(BookkeeperOptimisticLockException.class)
                .hasMessage("Transaction with id 5 can't be deleted");

        verify(repository).findById(5);
        verify(repository).delete(transaction);
    }
}
