package co.raccoons.bookkeeper.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TransactionOptimisticLockException extends RuntimeException {

    public TransactionOptimisticLockException(String message) {
        super(message);
    }
}
