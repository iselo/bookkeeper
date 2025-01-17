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

@RestControllerAdvice
@Slf4j
public final class BookkeeperExceptionHandler {

    @ExceptionHandler(BookkeeperNotFoundException.class)
    public ResponseEntity<BookkeeperOperationStatus> handle(HttpServletRequest request,
                                                            BookkeeperNotFoundException e) {
        return newResponseEntity(request, e, NOT_FOUND);
    }

    @ExceptionHandler(BookkeeperOptimisticLockException.class)
    public ResponseEntity<BookkeeperOperationStatus> handle(HttpServletRequest request,
                                                            BookkeeperOptimisticLockException e) {
        return newResponseEntity(request, e, CONFLICT);
    }

    private ResponseEntity<@Valid BookkeeperOperationStatus> newResponseEntity(HttpServletRequest request,
                                                                               RuntimeException e,
                                                                               HttpStatus httpStatus) {
        log.error(request.getRequestURL().toString() + "|" + e.getMessage() + "|" + httpStatus.toString());
        var transactionError = new BookkeeperOperationStatus(e.getMessage());
        return new ResponseEntity<>(transactionError, httpStatus);
    }
}
