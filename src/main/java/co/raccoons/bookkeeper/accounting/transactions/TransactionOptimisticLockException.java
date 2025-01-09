package co.raccoons.bookkeeper.accounting.transactions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
final class TransactionOptimisticLockException extends RuntimeException {

    public TransactionOptimisticLockException(String message) {
        super(message);
    }
}
