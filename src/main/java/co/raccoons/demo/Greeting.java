package co.raccoons.demo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@AllArgsConstructor
@Builder
@Getter
public final class Greeting {

    private final long id;
    private final String content;
}
