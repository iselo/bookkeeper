package co.raccoons.bookkeeper;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The class represents Bookkeeper application operation status for the requests
 * to which the repository does not return a value or throws an exception.
 * <p>
 * This class is bounded to the web request only.
 */
@ResponseBody
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class BookkeeperOperationStatus {

    @NotNull
    private String message;
}
