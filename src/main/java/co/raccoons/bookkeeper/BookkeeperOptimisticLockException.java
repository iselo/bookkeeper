package co.raccoons.bookkeeper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The exception for the situation when repository throws an exception on
 * the optimistic locking violation.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public final class BookkeeperOptimisticLockException extends RuntimeException {

    public BookkeeperOptimisticLockException(String message) {
        super(message);
    }
}
