package co.raccoons.bookkeeper.auth.signup;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public final class UserDuplicateException extends RuntimeException {

    public UserDuplicateException(String message) {
        super(message);
    }
}
