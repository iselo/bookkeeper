package co.raccoons.bookkeeper.accounting.charts;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/charts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountChartController {

    private final AccountChartRepository accountChartRepository;

    public AccountChartController(AccountChartRepository accountChartRepository) {
        this.accountChartRepository = accountChartRepository;
    }

    @GetMapping
    public Iterable<AccountChart> findAll() {
        return accountChartRepository.findAll();
    }

}
