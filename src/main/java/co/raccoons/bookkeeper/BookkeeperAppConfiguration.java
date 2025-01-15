package co.raccoons.bookkeeper;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("co.raccoons.bookkeeper")
@EnableAspectJAutoProxy
//@PropertySource("classpath:bookkeeper.properties")
public class BookkeeperAppConfiguration {

}
