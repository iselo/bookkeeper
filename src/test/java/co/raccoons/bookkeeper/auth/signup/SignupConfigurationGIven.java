package co.raccoons.bookkeeper.auth.signup;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SignupConfigurationGIven {

    @Bean
    public SignupRequest newSignupRequest(){
        return SignupRequest.builder()
                .name("Test User")
                .email("test@test.com")
                .password("passwordPassword")
                .build();
    }
}
