package uk.co.gosseyn.xanax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import uk.co.gosseyn.xanax.domain.GameObject;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class XanaxApplication {
	@Autowired
	private ApplicationContext applicationContext;
	public static void main(String[] args) {
		SpringApplication.run(XanaxApplication.class, args);
	}

	@PostConstruct
	public void init() {
		//TODO ApplicationContextAware directly on GameObject didn't work
		GameObject.setApplicationContext(applicationContext);
	}
}
