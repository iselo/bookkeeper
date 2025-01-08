package co.raccoons.bookkeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class BookkeeperApp {

	private BookkeeperApp() {
	}

	public static void main(String[] args) {
		SpringApplication.run(BookkeeperApp.class, args);
	}
}
