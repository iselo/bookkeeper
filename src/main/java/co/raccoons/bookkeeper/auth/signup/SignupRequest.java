package co.raccoons.bookkeeper.auth.signup;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class SignupRequest {

    @NotBlank(message = "Name cannot be blank")
    private final String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be blank")
    private final String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 1024 characters")
    private final String password;
}
