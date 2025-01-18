package co.raccoons.bookkeeper.auth.signup;

import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import co.raccoons.bookkeeper.MockMvcAwareTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SignupServiceTest extends MockMvcAwareTest {

    @MockBean
    private UserRepository repository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private SignupService service;

    @Autowired
    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        service = new SignupService(repository, passwordEncoder);
    }

    @Test
    @DisplayName("can signup")
    void returnsSuccessfulStatusOnSignUp() {
        when(repository.findById("test@test.com"))
                .thenReturn(Optional.empty());

        var status = new BookkeeperOperationStatus("User created");

        assertThat(service.signup(signupRequest))
                .isEqualTo(status);

        verify(repository).findById("test@test.com");
    }

    @Test
    @DisplayName("throws exception if user already exists")
    void throwsExceptionOnDuplicate() {
        when(repository.findById("test@test.com"))
                .thenReturn(Optional.of(User.builder()
                                .email("test@test.com")
                                .build()));

        assertThatThrownBy(() -> service.signup(signupRequest))
                .isInstanceOf(UserDuplicateException.class)
                .hasMessage("User already exists: test@test.com");

        verify(repository).findById("test@test.com");
    }
}