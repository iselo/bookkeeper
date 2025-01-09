package co.raccoons.bookkeeper.accounting.transactions;

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
final class TransactionExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<TransactionOperationStatus> handle(TransactionNotFoundException e) {
        return newResponseEntity(e, NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<TransactionOperationStatus> handle(TransactionOptimisticLockException e) {
        return newResponseEntity(e, CONFLICT);
    }

    private ResponseEntity<@Valid TransactionOperationStatus> newResponseEntity(RuntimeException e,
                                                                                HttpStatus httpStatus) {
        log.error(e.getMessage(), e);
        var transactionError = new TransactionOperationStatus(e.getMessage());
        return new ResponseEntity<>(transactionError, httpStatus);
    }
}
