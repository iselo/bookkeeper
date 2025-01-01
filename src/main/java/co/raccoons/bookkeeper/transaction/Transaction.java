package co.raccoons.bookkeeper.transaction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
final class Transaction {

    @Id
    @NotNull
    private  Integer id;

//    @NotNull
//    private java.sql.Date date;

    @NotNull
    private String description;

    @NotNull
    private  Integer account;

    @NotNull
    private  TransactionType type;

    @Positive
    private  BigDecimal amount;

    @NotNull
    private  Integer category;

    private  String notes;

    @Version
    private Long version;
}
