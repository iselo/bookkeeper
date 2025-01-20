package co.raccoons.bookkeeper.auth.signup;

import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
class SignupController {

    private SignupService service;

    @Autowired
    public SignupController(SignupService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookkeeperOperationStatus signup(@Valid @RequestBody SignupRequest signupRequest) {
        return service.signup(signupRequest);
    }
}
