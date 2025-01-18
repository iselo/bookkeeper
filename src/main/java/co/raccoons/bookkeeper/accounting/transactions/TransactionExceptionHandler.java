package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.BookkeeperExceptionHandler;
import co.raccoons.bookkeeper.BookkeeperNotFoundException;
import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import co.raccoons.bookkeeper.BookkeeperOptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
final class TransactionExceptionHandler extends BookkeeperExceptionHandler {

    /**
     * Handles exception when requested entity was not found in the repository.
     *
     * @param request HTTP request that triggers an exception
     * @param e an exception instance that was occurred
     * @return the response entity of {@code BookkeeperOperationStatus} for the HTTP request
     */
    @ExceptionHandler(BookkeeperNotFoundException.class)
    public ResponseEntity<BookkeeperOperationStatus> handle(HttpServletRequest request,
                                                            BookkeeperNotFoundException e) {
        return newResponseEntity(request, e, NOT_FOUND);
    }

    /**
     * Handles exception when repository operation throws an {@code OptimisticLockingFailureException}.
     *
     * @param request HTTP request that triggers an exception
     * @param e an exception instance that was occurred
     * @return the response entity of {@code BookkeeperOperationStatus} for the HTTP request
     */
    @ExceptionHandler(BookkeeperOptimisticLockException.class)
    public ResponseEntity<BookkeeperOperationStatus> handle(HttpServletRequest request,
                                                            BookkeeperOptimisticLockException e) {
        return newResponseEntity(request, e, CONFLICT);
    }
}
