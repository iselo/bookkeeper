package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import co.raccoons.bookkeeper.BookkeeperOptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

/**
 * The transaction service layer for interacting with repository.
 */
@Service
class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Returns all transactions form repository.
     */
    public Iterable<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    /**
     * Returns the transaction form repository for the given id if such is present,
     * otherwise throws an exception {@code BookkeeperNotFoundException}.
     *
     * @param id the id of the transaction to look up in the repository
     * @return an instance of the {@code Transaction}
     * @throws TransactionNotFoundException if transaction not found
     */
    @Transactional
    public Transaction findById(Integer id) throws TransactionNotFoundException {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(
                        format("Transaction with id %s not found", id)));
    }

    /**
     *  Creates given transaction in the repository.
     *
     * @param transaction the transaction to be created in the repository
     * @return an instance of the {@code Transaction}
     * @throws BookkeeperOptimisticLockException on optimistic locking violation
     */
    @Transactional
    public Transaction create(Transaction transaction) throws BookkeeperOptimisticLockException {
        try {
            return transactionRepository.save(transaction);
        } catch (OptimisticLockingFailureException e) {
            throw new BookkeeperOptimisticLockException("Transaction can't be created");
        }
    }

    /**
     * Updates given transaction in the repository.
     *
     * @param transaction the transaction to be updated in the repository
     * @return an instance of the updated transaction
     * @throws BookkeeperOptimisticLockException on optimistic locking violation
     */
    @Transactional
    public Transaction update(Transaction transaction) throws BookkeeperOptimisticLockException {
        try {
            return transactionRepository.save(transaction);
        } catch (OptimisticLockingFailureException e) {
            var message = format("Transaction with id %s can't be updated", transaction.getId());
            throw new BookkeeperOptimisticLockException(message);
        }
    }

    /**
     * Deletes transaction specified with id in the repository.
     *
     * @param id the transaction id to be deleted
     * @return a new instance of successful delete operation status
     * @throws BookkeeperOptimisticLockException on optimistic locking violation
     */
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

