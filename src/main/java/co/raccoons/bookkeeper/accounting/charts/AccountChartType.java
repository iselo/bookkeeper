package co.raccoons.bookkeeper.accounting.charts;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@Getter
@ToString
@Builder
@Document
public class AccountChartType {

    @Id
    @NotNull
    private final Integer id;

    @NotNull
    private final AccountChartTypeGroup group;

    @NotNull
    @NotEmpty
    private final String name;
}
