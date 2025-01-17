package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.BookkeeperNotFoundException;
import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import co.raccoons.bookkeeper.BookkeeperOptimisticLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Service
class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Iterable<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction findById(Integer id) throws BookkeeperNotFoundException {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new BookkeeperNotFoundException(
                        format("Transaction with id %s not found", id)));
    }

    @Transactional
    public Transaction create(Transaction transaction) throws BookkeeperOptimisticLockException {
        try {
            return transactionRepository.save(transaction);
        } catch (OptimisticLockingFailureException e) {
            throw new BookkeeperOptimisticLockException("Transaction can't be created");
        }
    }

    @Transactional
    public Transaction update(Transaction transaction) throws BookkeeperOptimisticLockException {
        try {
            return transactionRepository.save(transaction);
        } catch (OptimisticLockingFailureException e) {
            var message = format("Transaction with id %s can't be updated", transaction.getId());
            throw new BookkeeperOptimisticLockException(message);
        }
    }

    @Transactional
    public BookkeeperOperationStatus delete(Integer id) throws BookkeeperOptimisticLockException {
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

