package co.raccoons.bookkeeper.accounting.transactions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDate;

@ResponseBody
@Document("Transaction")
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Builder
final class Transaction {

    @Id
    @NotNull
    private final Integer id;

    @NotNull
    private final LocalDate occurredOn;

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
