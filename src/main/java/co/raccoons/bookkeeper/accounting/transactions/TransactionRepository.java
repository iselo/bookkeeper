package co.raccoons.bookkeeper.accounting.transactions;

import org.springframework.data.mongodb.repository.MongoRepository;

interface TransactionRepository extends MongoRepository<Transaction, Integer> {
}
