package co.raccoons.bookkeeper.transaction;

import org.springframework.data.repository.CrudRepository;

interface TransactionRepository extends CrudRepository<Transaction, Integer> {
}
