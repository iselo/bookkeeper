package co.raccoons.bookkeeper.transactions;

import co.raccoons.bookkeeper.MockMvcAwareTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.sql.Date;

import static com.google.common.truth.Truth.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest extends MockMvcAwareTest {

    @Test
    @DisplayName("findById throws transaction not found exception")
    void findByIdThrowsExceptionAndReturnsHttp404() throws Exception {
        perform(
                get("/transactions/0")
        )
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(TransactionNotFoundException.class)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("{statusCode:404,message:\"Transaction with id 0 not found\"}"));
    }

    @Test
    @DisplayName("creates transaction")
    void createsTransactionAndReturnsHttp201() throws Exception {
        perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(
                                Transaction.builder()
                                        .id(1)
                                        .description("Internet-GPON")
                                        .occurredOn(Date.valueOf("2025-01-01"))
                                        .account(100)
                                        .type(TransactionType.DEPOSIT)
                                        .category(500)
                                        .amount(BigDecimal.valueOf(0.99))
                                        .build()
                        ))
        )
                .andExpect(status().isCreated())
                .andExpect(content().json("{id:1,occurredOn:2025-01-01,description:Internet-GPON,account:100,type:DEPOSIT,category:500,amount:0.99}"));
    }

    @Test
    @DisplayName("finds transaction by id")
    void findsByIdAndReturnsHttp200() throws Exception {
        perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(
                                Transaction.builder()
                                        .id(2)
                                        .occurredOn(Date.valueOf("2025-01-01"))
                                        .account(100)
                                        .description("Internet-GPON")
                                        .type(TransactionType.DEPOSIT)
                                        .amount(BigDecimal.valueOf(0.99))
                                        .category(500)
                                        .build()
                        ))
        );
        perform(
                get("/transactions/2")
        )
                .andExpect(status().isOk())
                .andExpect(content().json("{id:2,occurredOn:2025-01-01,description:Internet-GPON,account:100,type:DEPOSIT,category:500,amount:0.99}"));
    }

    @Test
    @DisplayName("updates transaction by id")
    void updatesTransactionAndReturnsHttp204() throws Exception {
        perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(
                                Transaction.builder()
                                        .id(3)
                                        .occurredOn(Date.valueOf("2025-01-01"))
                                        .description("Old")
                                        .account(100)
                                        .type(TransactionType.DEPOSIT)
                                        .amount(BigDecimal.valueOf(0.99))
                                        .category(500)
                                        .build()
                        ))
        );
        perform(
                put("/transactions/3")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(
                                Transaction.builder()
                                        .id(3)
                                        .occurredOn(Date.valueOf("2025-01-01"))
                                        .description("New")
                                        .account(103)
                                        .type(TransactionType.WITHDRAWAL)
                                        .amount(BigDecimal.valueOf(0.99))
                                        .category(501)
                                        .version(0L)
                                        .build()
                        ))

        )
                .andExpect(status().isOk());

        perform(
                get("/transactions/3")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("{id:3,occurredOn:2025-01-01,description:New,account:103,type:WITHDRAWAL,category:501,amount:0.99}"));
    }

    @Test
    @DisplayName("handles optimistic lock error")
    void throwsOptimisticLockExceptionAndReturnsHttp409() throws Exception {
        perform(
                put("/transactions/3")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(
                                Transaction.builder()
                                        .id(3)
                                        .occurredOn(Date.valueOf("2025-01-01"))
                                        .description("Some description")
                                        .account(103)
                                        .type(TransactionType.WITHDRAWAL)
                                        .amount(BigDecimal.valueOf(0.99))
                                        .category(500)
                                        .version(0L)
                                        .build()
                        ))

        )
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(TransactionOptimisticLockException.class))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("{statusCode:409,message:\"Transaction with id 3 can't be updated due to optimistic lock\"}"));
    }

    @Test
    @DisplayName("deletes transaction by id")
    void deletesTransactionAndReturnsHttp204() throws Exception {
        perform(
                post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(toJson(
                                Transaction.builder()
                                        .id(9)
                                        .occurredOn(Date.valueOf("2025-01-01"))
                                        .account(100)
                                        .description("Description sample")
                                        .type(TransactionType.DEPOSIT)
                                        .amount(BigDecimal.valueOf(0.99))
                                        .category(500)
                                        .build()
                        ))
        );

        perform(
                delete("/transactions/9")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("{statusCode:200, message:'Successfully deleted'}"));

        perform(
                get("/transactions/9")
        )
                .andExpect(status().isNotFound());
    }
}
