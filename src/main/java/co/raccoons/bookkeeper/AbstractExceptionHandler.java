package co.raccoons.bookkeeper;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * An abstract Bookkeeper exception handler.
 */
@Slf4j
public abstract class AbstractExceptionHandler {

    protected ResponseEntity<BookkeeperOperationStatus> newResponseEntity(HttpServletRequest request,
                                                                          Throwable e,
                                                                          HttpStatus httpStatus) {
        log.error(request.getRequestURL().toString() + " | " + e.getMessage() + " | " + httpStatus.toString());
        var transactionError = new BookkeeperOperationStatus(e.getMessage());
        return new ResponseEntity<>(transactionError, httpStatus);
    }
}
