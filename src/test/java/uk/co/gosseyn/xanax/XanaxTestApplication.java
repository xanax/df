package uk.co.gosseyn.xanax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import uk.co.gosseyn.xanax.domain.GameObject;
import uk.co.gosseyn.xanax.service.BitcoinService;
import uk.co.gosseyn.xanax.service.GameService;
import uk.co.gosseyn.xanax.service.IdService;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
})
@Profile("test")
public class XanaxTestApplication {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private IdService bitcoinService;

	@Autowired
	private GameService gameService;

	public static void main(String[] args) {
		SpringApplication.run(XanaxTestApplication.class, args);
	}

	@PostConstruct
	public void init() throws IOException, InterruptedException {
		//TODO ApplicationContextAware directly on GameObject didn't work
		GameObject.setApplicationContext(applicationContext);

		//gameService.gameLoop();

	}
}
