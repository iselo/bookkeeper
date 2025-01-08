package co.raccoons.bookkeeper.transactions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.math.BigDecimal;
import java.sql.Date;

@AllArgsConstructor
@Getter
@Builder
final class Transaction {

    @Id
    @NotNull
    private final Integer id;

    @NotNull
    private Date occurredOn;

    @NotNull
    private final String description;

    @NotNull
    @Positive
    private final Integer account;

    @NotNull
    private final TransactionType type;

    @Positive
    private final BigDecimal amount;

    @NotNull
    @Positive
    private final Integer category;

    private final String notes;

    @Version
    private final Long version;
}
