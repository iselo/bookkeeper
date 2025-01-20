package co.raccoons.bookkeeper.accounting.charts;

import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import co.raccoons.bookkeeper.BookkeeperOptimisticLockException;
import co.raccoons.bookkeeper.accounting.transactions.TransactionNotFoundException;
import javax.validation.Valid;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

@RestController
@RequestMapping(path = "/charts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountChartController {

    private final AccountChartRepository accountChartRepository;

    public AccountChartController(AccountChartRepository accountChartRepository) {
        this.accountChartRepository = accountChartRepository;
    }

    @GetMapping
    public Iterable<AccountChart> findAll() {
        return accountChartRepository.findAll();
    }

    @GetMapping("/{id}")
    @Valid
    public AccountChart findById(@PathVariable Integer id) {
        return accountChartRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(
                        format("Account chart with id %s not found", id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Valid
    public AccountChart create(@Valid @RequestBody AccountChart accountChart) {
        try {
            return accountChartRepository.save(accountChart);
        } catch (OptimisticLockingFailureException e) {
            throw new BookkeeperOptimisticLockException("Account chart can't be created");
        }
    }

    @DeleteMapping("/{id}")
    @Valid
    public BookkeeperOperationStatus delete(@PathVariable Integer id) {
        try {
            var transaction = findById(id);
            accountChartRepository.delete(transaction);
            return new BookkeeperOperationStatus("Successfully deleted");
        } catch (OptimisticLockingFailureException e) {
            var message = format("Transaction with id %s can't be deleted", id);
            throw new BookkeeperOptimisticLockException(message);
        }
    }

}
