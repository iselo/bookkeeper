package co.raccoons.bookkeeper.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

//@Repository
@Slf4j
public class JdbcTransactionRepository {

    private final JdbcTemplate sqlClient;

    public JdbcTransactionRepository(JdbcTemplate sqlClient) {
        this.sqlClient = checkNotNull(sqlClient);
    }

    List<Transaction> finaAll() {
        var sql = "SELECT * FROM TRANSACTION";
        return sqlClient.query(sql, JdbcTransactionRepository::mapRow);
    }

    Optional<Transaction> findById(Integer id) {
        try {
            var sql = "SELECT * FROM TRANSACTION WHERE ID=?";
            var transaction = sqlClient.queryForObject(sql, JdbcTransactionRepository::mapRow, id);
            return Optional.ofNullable(transaction);
        } catch (DataAccessException e) {
            log.info("Requested transaction id: " + id);
        }
        return Optional.empty();
    }

    void create(Transaction transaction) {
        var sql = "INSERT INTO TRANSACTION(ID, ACCOUNT, TYPE, AMOUNT) values(?, ?, ?, ?)";
        var transactionId = transaction.getId();
        var affectedRowCount = sqlClient.update(sql, transactionId, transaction.getAccount(), transaction.getType(), transaction.getAmount());
        if (affectedRowCount == 1) {
            log.info("New transaction inserted: " + transactionId);
        }
    }

    void update(Transaction transaction, Integer id) {
        var sql = "UPDATE TRANSACTION SET ACCOUNT=?, TYPE=?, AMOUNT=? WHERE ID=?";
        var affectedRowCount = sqlClient.update(sql, id, transaction.getAccount(), transaction.getType(), transaction.getAmount());
        if (affectedRowCount == 1) {
            log.info("Transaction updated: " + id);
        }
    }

    void delete(Integer id) {
        var sql = "DELETE FROM TRANSACTION WHERE ID=?";
        var affectedRowCount = sqlClient.update(sql, id);
        if (affectedRowCount == 1) {
            log.info("Transaction updated: " + id);
        }
    }

    private static Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Transaction.builder()
                .id(rs.getInt("id"))
                .account(rs.getInt("account"))
                .type(TransactionType.valueOf(rs.getString("type")))
                .amount(rs.getBigDecimal("amount"))
                .build();
    }
}
