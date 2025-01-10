package co.raccoons.bookkeeper.accounting.transactions;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.sql.Date;

@TestConfiguration
public class TransactionConfigurationGiven {

    @Bean
    public Transaction newTransaction() {
        return Transaction.builder()
                .id(5)
                .description("Internet-LTE")
                .occurredOn(Date.valueOf("2025-01-01"))
                .account(100)
                .type(TransactionType.DEPOSIT)
                .category(500)
                .amount(BigDecimal.valueOf(0.99))
                .build();
    }
}
