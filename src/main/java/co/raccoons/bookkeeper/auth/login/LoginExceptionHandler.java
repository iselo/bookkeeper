package co.raccoons.bookkeeper.auth.login;

import co.raccoons.bookkeeper.AbstractExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
final class LoginExceptionHandler extends AbstractExceptionHandler {

    /*
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<BookkeeperOperationStatus> handle(HttpServletRequest request,
                                                            BadCredentialsException e) {
        return newResponseEntity(request, e, UNAUTHORIZED);
    }

     */
}
