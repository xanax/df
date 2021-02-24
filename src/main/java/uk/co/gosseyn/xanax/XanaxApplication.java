package uk.co.gosseyn.xanax;

import lombok.Setter;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import uk.co.gosseyn.xanax.domain.GameObject;
import uk.co.gosseyn.xanax.service.BitcoinService;
import uk.co.gosseyn.xanax.service.GameService;
import uk.co.gosseyn.xanax.service.WalletHolder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

@SpringBootApplication
@EnableAsync
public class XanaxApplication {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private BitcoinService bitcoinService;

	@Autowired
	private GameService gameService;

	public static void main(String[] args) {
		SpringApplication.run(XanaxApplication.class, args);
	}

	@PostConstruct
	public void init() throws IOException, InterruptedException {
		//TODO ApplicationContextAware directly on GameObject didn't work
		GameObject.setApplicationContext(applicationContext);

		gameService.gameLoop();

	}
}
