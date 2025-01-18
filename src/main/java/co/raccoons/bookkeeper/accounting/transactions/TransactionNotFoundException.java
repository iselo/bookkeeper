package co.raccoons.bookkeeper.accounting.transactions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The exception for the situation when requested entity is not found in
 * the repository.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public final class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
