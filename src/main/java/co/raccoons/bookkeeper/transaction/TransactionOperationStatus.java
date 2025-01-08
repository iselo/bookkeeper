package co.raccoons.bookkeeper.transaction;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Builder
@Getter
public final class TransactionOperationStatus {

    @NotNull
    @Positive
    private Integer statusCode;

    @NotNull
    private String message;
}
