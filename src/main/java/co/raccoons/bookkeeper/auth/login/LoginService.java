package co.raccoons.bookkeeper.auth.login;

import org.springframework.stereotype.Service;

@Service
class LoginService {

/*
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) throws BadCredentialsException{
        return LoginResponse.builder().build();
    }
        try {
            var email = loginRequest.getEmail();
            var password = loginRequest.getPassword();
            var token = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            addLoginAttempt(loginRequest.getEmail(), false);
            throw e;
        }

        var email = loginRequest.getEmail();
        var token = JwtHelper.generateToken(email);
        addLoginAttempt(email, true);
        return new LoginResponse(email, token));
    }

    @Transactional
    private void addLoginAttempt(String email, boolean success) {
        var loginAttempt = new LoginAttempt(email, success, LocalDateTime.now());
        repository.add(loginAttempt);
    }

    public List<LoginAttempt> findRecentLoginAttempts(String email) {
        return repository.findRecent(email);
    }

 */
}
