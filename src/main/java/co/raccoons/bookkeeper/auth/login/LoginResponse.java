package co.raccoons.bookkeeper.auth.login;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class LoginResponse {

    @NotNull
    private final String email;

    @NotNull
    private final String token;
}
