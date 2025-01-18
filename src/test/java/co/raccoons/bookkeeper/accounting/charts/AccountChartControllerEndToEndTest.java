package co.raccoons.bookkeeper.accounting.charts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "PT15S")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountChartControllerEndToEndTest {

    @LocalServerPort
    private Integer randomPort;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var accountChart = AccountChart.builder()
                .id(1)
                .name("Debit card")
                .description("descr")
                .type(AccountChartType.builder().build())
                .build();
        webTestClient.post()
                .uri("/charts")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(accountChart)
                .exchange();
    }

    @AfterEach
    void tearDown() {
        webTestClient.delete().uri("/charts/1").exchange();
        webTestClient.delete().uri("/charts/2").exchange();
    }

    @Test
    @Order(1)
    @DisplayName("finds all charts")
    void findsAllTransaction() {
        webTestClient.get()
                .uri("/charts")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.size()").isEqualTo(1);
    }

    @Test
    @Order(2)
    @DisplayName("creates account chart")
    void createsTransactionAndReturnsHttp201() {
        var accountChart = AccountChart.builder()
                .id(2)
                .name("Debit card 2")
                .description("descr")
                .type(AccountChartType.builder().build())
                .build();
        webTestClient.post()
                .uri("/charts")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(accountChart)
                .exchange()
                .expectStatus().isCreated();
    }
}
