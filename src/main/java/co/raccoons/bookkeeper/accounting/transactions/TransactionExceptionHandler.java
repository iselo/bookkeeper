package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.AbstractExceptionHandler;
import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import co.raccoons.bookkeeper.BookkeeperOptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
final class TransactionExceptionHandler extends AbstractExceptionHandler {

    /**
     * Handles exception when requested entity was not found in the repository.
     *
     * @param request HTTP request that triggers an exception
     * @param e       an exception instance that was occurred
     * @return the response entity of {@code BookkeeperOperationStatus} for the HTTP request
     */
    @ExceptionHandler(TransactionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<BookkeeperOperationStatus> handle(HttpServletRequest request,
                                                            TransactionNotFoundException e) {
        return newResponseEntity(request, e, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exception when repository operation throws an {@code OptimisticLockingFailureException}.
     *
     * @param request HTTP request that triggers an exception
     * @param e       an exception instance that was occurred
     * @return the response entity of {@code BookkeeperOperationStatus} for the HTTP request
     */
    @ExceptionHandler(BookkeeperOptimisticLockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<BookkeeperOperationStatus> handle(HttpServletRequest request,
                                                            BookkeeperOptimisticLockException e) {
        return newResponseEntity(request, e, HttpStatus.CONFLICT);
    }
}
