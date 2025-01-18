package co.raccoons.bookkeeper.accounting.charts;

import co.raccoons.bookkeeper.MockMvcAwareTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
class AccountChartControllerTest extends MockMvcAwareTest {

    @MockBean
    private AccountChartRepository repository;

    @Test
    @DisplayName("finds all account charts")
    void findsAllAccountChart() throws Exception {
        var charts = new ArrayList<>();
        charts.add(AccountChart.builder().build());
        doReturn(charts)
                .when(repository)
                .findAll();
        perform(get("/charts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.size()", is(charts.size())));
    }

    @Test
    @DisplayName("creates account chart")
    void createsAccountChartAndReturnsHttp201() throws Exception {
        var accountChart = AccountChart.builder()
                .id(1)
                .name("Debit card")
                .description("descr")
                .type(AccountChartType.builder().build())
                .build();

        perform(post("/charts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(accountChart))
        )
                .andExpect(status().isCreated());
    }
}
