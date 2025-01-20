package co.raccoons.bookkeeper.auth.signup;

import co.raccoons.bookkeeper.AbstractExceptionHandler;
import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
final class SignupExceptionHandler extends AbstractExceptionHandler {

    @ExceptionHandler(UserDuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<BookkeeperOperationStatus> handle(HttpServletRequest request,
                                                            UserDuplicateException e) {
        return newResponseEntity(request, e, HttpStatus.CONFLICT);
    }
}
