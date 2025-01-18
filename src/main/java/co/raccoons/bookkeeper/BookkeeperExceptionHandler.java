package co.raccoons.bookkeeper;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class BookkeeperExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BookkeeperOperationStatus> handle(HttpServletRequest request,
                                                            MethodArgumentNotValidException e) {
        return newResponseEntity(request, e, HttpStatus.BAD_REQUEST);

    }
}