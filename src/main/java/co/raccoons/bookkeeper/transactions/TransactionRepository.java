package co.raccoons.bookkeeper.transactions;

import org.springframework.data.repository.CrudRepository;

interface TransactionRepository extends CrudRepository<Transaction, Integer> {
}
