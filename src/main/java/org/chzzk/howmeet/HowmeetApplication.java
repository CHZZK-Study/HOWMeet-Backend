package org.chzzk.howmeet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class HowmeetApplication {

	public static void main(String[] args) {
		SpringApplication.run(HowmeetApplication.class, args);
	}

}
