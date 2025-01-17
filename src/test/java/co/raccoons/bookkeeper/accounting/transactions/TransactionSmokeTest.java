package co.raccoons.bookkeeper.accounting.transactions;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.google.common.truth.Truth.assertThat;

@SpringBootTest
class TransactionSmokeTest {

    @Autowired
    public TransactionController controller;

    @Test
    void contextLoad() throws Exception {
        assertThat(controller).isNotNull();
    }
}
