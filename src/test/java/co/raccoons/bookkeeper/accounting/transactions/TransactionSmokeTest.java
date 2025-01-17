package co.raccoons.bookkeeper.accounting.transactions;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransactionSmokeTest {

    @Autowired
    private TransactionController controller;

    @Test
    void contextLoad() throws Exception {
        assertThat(controller).isNotNull();
    }
}
