package co.raccoons.bookkeeper.transaction;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Builder
@Getter
public final class TransactionOperationStatus {

    private int statusCode;
    private String message;
}
