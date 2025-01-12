package co.raccoons.bookkeeper;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@AllArgsConstructor
@Getter
public final class BookkeeperOperationStatus {

    @NotNull
    private String message;
}
