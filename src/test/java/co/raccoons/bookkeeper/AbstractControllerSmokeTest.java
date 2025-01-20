package co.raccoons.bookkeeper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public abstract class AbstractControllerSmokeTest<C> {

    @Autowired
    private C controller;

    @Test
    @DisplayName("Controller is autowired")
    void contextLoad() throws Exception {
        assertThat(controller).isNotNull();
    }
}
