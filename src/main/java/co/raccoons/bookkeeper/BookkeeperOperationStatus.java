package co.raccoons.bookkeeper;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class BookkeeperOperationStatus {

    @NotNull
    private String message;
}
