package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.BookkeeperNotFoundException;
import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import co.raccoons.bookkeeper.BookkeeperOptimisticLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
final class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Iterable<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction findById(Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new BookkeeperNotFoundException(
                        format("Transaction with id %s not found", id)));
    }

    public Transaction create(Transaction transaction) {
        try {
            return transactionRepository.save(transaction);
        } catch (OptimisticLockingFailureException e) {
            throw new BookkeeperOptimisticLockException("Transaction can't be created");
        }
    }

    public Transaction update(Transaction transaction) {
        try {
            return transactionRepository.save(transaction);
        } catch (OptimisticLockingFailureException e) {
            var message = format("Transaction with id %s can't be updated", transaction.getId());
            throw new BookkeeperOptimisticLockException(message);
        }
    }

    public BookkeeperOperationStatus delete(Integer id) {
        try {
            var transaction = findById(id);
            transactionRepository.delete(transaction);
            return new BookkeeperOperationStatus("Successfully deleted");
        } catch (OptimisticLockingFailureException e) {
            var message = format("Transaction with id %s can't be deleted", id);
            throw new BookkeeperOptimisticLockException(message);
        }
    }
}

