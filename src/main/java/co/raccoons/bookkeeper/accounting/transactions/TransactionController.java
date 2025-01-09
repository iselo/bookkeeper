package co.raccoons.bookkeeper.accounting.transactions;

import javax.validation.Valid;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

@RestController
@RequestMapping(path = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public Iterable<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @GetMapping("/{id}")
    @Valid
    public Transaction findById(@PathVariable Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(
                        format("Transaction with id %s not found", id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Valid
    public Transaction create(@Valid @RequestBody Transaction transaction) {
        try {
            return transactionRepository.save(transaction);
        } catch (OptimisticLockingFailureException e) {
            throw new TransactionOptimisticLockException("Transaction can't be created");
        }
    }

    @PutMapping("/{id}")
    @Valid
    public Transaction update(@Valid @RequestBody Transaction transaction, @PathVariable Integer id) {
        try {
            return transactionRepository.save(transaction);
        } catch (OptimisticLockingFailureException e) {
            var message = format("Transaction with id %s can't be updated", id);
            throw new TransactionOptimisticLockException(message);
        }
    }

    @DeleteMapping("/{id}")
    @Valid
    public TransactionOperationStatus delete(@PathVariable Integer id) {
        try {
            var transaction = findById(id);
            transactionRepository.delete(transaction);
            return new TransactionOperationStatus("Successfully deleted");
        } catch (OptimisticLockingFailureException e) {
            var message = format("Transaction with id %s can't be deleted", id);
            throw new TransactionOptimisticLockException(message);
        }
    }
}
