package co.raccoons.bookkeeper.accounting.transactions;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * The repository of transactions to be persisted in the MongoDB.
 */
interface TransactionRepository extends MongoRepository<Transaction, Integer> {
}
