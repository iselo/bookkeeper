package co.raccoons.bookkeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class BookkeeperApp {

	public static void main(String[] args) {
		SpringApplication.run(BookkeeperApp.class, args);
	}
}
