package co.raccoons.bookkeeper.auth.login;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
class LoginController {

    private final LoginService service;

    public LoginController(LoginService service) {
        this.service = service;
    }

    @PostMapping
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return LoginResponse.builder().build();
//                service.login(loginRequest);
    }

}
