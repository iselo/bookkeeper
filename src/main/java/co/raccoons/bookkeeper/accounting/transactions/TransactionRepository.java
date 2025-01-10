package co.raccoons.bookkeeper.accounting.transactions;

import org.springframework.data.repository.CrudRepository;

interface TransactionRepository extends CrudRepository<Transaction, Integer> {
}
