package co.raccoons.bookkeeper.transaction;

import javax.validation.Valid;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public Iterable<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Valid
    public Transaction findById(@PathVariable Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id " +
                        id + " not found"));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Valid
    public Transaction create(@Valid @RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Valid
    public Transaction update(@Valid @RequestBody Transaction transaction, @PathVariable Integer id) {
        try {
            return transactionRepository.save(transaction);
        } catch (OptimisticLockingFailureException e) {
            throw new TransactionOptimisticLockException("Transaction with id " +
                    id + " can't be updated due to optimistic lock");
        }
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<@Valid TransactionOperationStatus> delete(@PathVariable Integer id) {
        try {
            var transaction = findById(id);
            transactionRepository.delete(transaction);
            var status = TransactionOperationStatus.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully deleted")
                    .build();
            return ResponseEntity.ok(status);
        } catch (OptimisticLockingFailureException e) {
            throw new TransactionOptimisticLockException("Transaction with id " +
                    id + " can't be deleted due to optimistic lock");
        }
    }
}
