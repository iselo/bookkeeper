package co.raccoons.bookkeeper.accounting.charts;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
@ToString
@Builder
public class AccountType {

    @Id
    @NotNull
    private final Integer id;

    @NotNull
    private final AccountTypeGroup group;

    @NotNull
    @NotEmpty
    private final String name;
}
