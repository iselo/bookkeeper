package co.raccoons.bookkeeper.auth.signup;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Document("User")
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class User {

    @NotNull
    private final String name;

    @Id
    @NotNull
    private final String email;

    @NotNull
    private final String password;

    @Version
    private final Long version;
}
