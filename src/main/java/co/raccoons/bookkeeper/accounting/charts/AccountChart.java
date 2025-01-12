package co.raccoons.bookkeeper.accounting.charts;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Document("AccountChart")
final class AccountChart {

    @Id
    @NotNull
    private final Integer id;

//    @NotNull
//    private final AccountChartType type;

    @NotNull
    @NotEmpty
    private final String name;

    private final String description;

    @Version
    private final Long version;
}
