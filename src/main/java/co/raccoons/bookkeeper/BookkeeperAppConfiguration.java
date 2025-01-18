package co.raccoons.bookkeeper;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * The Bookkeeper application configuration.
 */
@Configuration
@ComponentScan("co.raccoons.bookkeeper")
@EnableAspectJAutoProxy
public class BookkeeperAppConfiguration {
}
