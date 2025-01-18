package co.raccoons.bookkeeper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * The Bookkeeper application.
 */
@SpringBootApplication
@EnableMongoRepositories
@Slf4j
public class BookkeeperApp {

	/**
	 * The Bookkeeper application entry point.
	 */
	public static void main(String[] args) {
		log.info("Nice");
		SpringApplication.run(BookkeeperApp.class, args);
	}
}
