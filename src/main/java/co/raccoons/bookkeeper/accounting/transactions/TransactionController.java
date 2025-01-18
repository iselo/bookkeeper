package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * The transaction REST controller.
 */
@RestController
@RequestMapping(path = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Handles HTTP GET request to the '/transactions' endpoint.
     */
    @GetMapping
    public Iterable<Transaction> findAll() {
        return transactionService.findAll();
    }

    /**
     * Handles HTTP GET request to the '/transactions/{id}' endpoint.
     */
    @GetMapping("/{id}")
    @Valid
    public Transaction findById(@PathVariable Integer id) {
        return transactionService.findById(id);
    }

    /**
     * Handles HTTP POST request to the '/transactions' endpoint.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Valid
    public Transaction create(@Valid @RequestBody Transaction transaction) {
        return transactionService.create(transaction);
    }

    /**
     * Handles HTTP PUT request to the '/transactions' endpoint.
     */
    @PutMapping
    @Valid
    public Transaction update(@Valid @RequestBody Transaction transaction) {
        return transactionService.update(transaction);
    }

    /**
     * Handles HTTP DELETE request to the '/transactions/{id}' endpoint.
     */
    @DeleteMapping("/{id}")
    @Valid
    public BookkeeperOperationStatus delete(@PathVariable Integer id) {
        return transactionService.delete(id);
    }
}
