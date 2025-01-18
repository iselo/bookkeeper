package co.raccoons.bookkeeper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * The Bookkeeper exception handler.
 */
@RestControllerAdvice
@Slf4j
final class BookkeeperExceptionHandler {

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

    private ResponseEntity<@Valid BookkeeperOperationStatus> newResponseEntity(HttpServletRequest request,
                                                                               RuntimeException e,
                                                                               HttpStatus httpStatus) {
        log.error(request.getRequestURL().toString() + " | " + e.getMessage() + " | " + httpStatus.toString());
        var transactionError = new BookkeeperOperationStatus(e.getMessage());
        return new ResponseEntity<>(transactionError, httpStatus);
    }
}
