package co.raccoons.bookkeeper.auth.signup;

import co.raccoons.bookkeeper.BookkeeperOperationStatus;
import co.raccoons.bookkeeper.MockMvcAwareTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
class SignupControllerTest extends MockMvcAwareTest {

    @MockBean
    private SignupService service;

    @Autowired
    private SignupRequest signupRequest;

    @Test
    @DisplayName("can signup")
    void createsUserAndReturnsHttp201() throws Exception {
        var status = new BookkeeperOperationStatus("User created");

        when(service.signup(signupRequest))
                .thenReturn(status);

        perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(signupRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("User created"));

        verify(service).signup(signupRequest);
    }

    @Test
    @DisplayName("throws exception if user already exists")
    void throwsExceptionAndReturnsHttp409() throws Exception {
        when(service.signup(signupRequest))
                .thenThrow(new UserDuplicateException("User already exists: " + signupRequest.getEmail()));

        perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(signupRequest))
        )
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(UserDuplicateException.class))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message").value("User already exists: test@test.com"));

        verify(service).signup(signupRequest);
    }

    @ParameterizedTest
    @MethodSource("invalidSignupRequest")
    @DisplayName("not accept invalid request")
    void notAcceptInvalidRequest(SignupRequest signupRequest) throws Exception {
        perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(signupRequest))
        )
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(MethodArgumentNotValidException.class))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(startsWith("Validation failed for argument")));
    }

    private static Stream<Arguments> invalidSignupRequest() {
        String password130Symbols = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        return Stream.of(
                Arguments.of(SignupRequest.builder().email(null).password("0123456789").name("Name").build()),
                Arguments.of(SignupRequest.builder().email("test@test.com").password(null).name("Name").build()),
                Arguments.of(SignupRequest.builder().email("test@test.com").password("0123456789").name(null).build()),
                Arguments.of(SignupRequest.builder().email("").password("0123456789").name("Name").build()),
                Arguments.of(SignupRequest.builder().email("test@test.com").password("").name("Name").build()),
                Arguments.of(SignupRequest.builder().email("test@test.com").password("0123456789").name("").build()),
                Arguments.of(SignupRequest.builder().email("test@test.com").password("0123456").name("Name").build()),
                Arguments.of(SignupRequest.builder().email("test@test.com").password(password130Symbols).name("Name").build())
        );
    }

}
