package co.raccoons.bookkeeper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public final class BookkeeperOptimisticLockException extends RuntimeException {

    public BookkeeperOptimisticLockException(String message) {
        super(message);
    }
}
