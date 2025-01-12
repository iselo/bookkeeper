package co.raccoons.bookkeeper.accounting.charts;

import org.springframework.data.mongodb.repository.MongoRepository;

interface AccountChartRepository extends MongoRepository<AccountChart, Integer> {
}
