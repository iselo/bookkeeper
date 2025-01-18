package co.raccoons.bookkeeper.auth.signup;


import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
class SignupService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignupService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public BookkeeperOperationStatus signup(SignupRequest signupRequest) throws UserDuplicateException {
        maybeUser(signupRequest).ifPresent(SignupService::throwDuplicateException);
        var newUser = newUser(signupRequest);
        repository.save(newUser);
        return new BookkeeperOperationStatus("User created");
    }

    private Optional<User> maybeUser(SignupRequest signupRequest) {
        var email = signupRequest.getEmail();
        return repository.findById(email);
    }

    private User newUser(SignupRequest signupRequest) {
        var rawPassword = signupRequest.getPassword();
        var hashedPassword = passwordEncoder.encode(rawPassword);
        return User.builder()
                .name(signupRequest.getName())
                .email(signupRequest.getEmail())
                .password(hashedPassword)
                .build();
    }

    private static void throwDuplicateException(User user) {
        throw new UserDuplicateException("User already exists: " + user.getEmail());
    }
}
