package co.raccoons.bookkeeper.accounting.transactions;

import co.raccoons.bookkeeper.MockMvcAwareTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)

class TransactionServiceTest extends MockMvcAwareTest {

    @MockBean
    private TransactionRepository transactionRepository;

    private Transaction transaction;

}