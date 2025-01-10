package co.raccoons.bookkeeper.accounting.transactions;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@AllArgsConstructor
@Getter
final class TransactionOperationStatus {

    @NotNull
    private String message;
}
