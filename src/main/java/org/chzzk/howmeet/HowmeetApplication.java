package org.chzzk.howmeet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@EnableRedisRepositories
@SpringBootApplication
public class HowmeetApplication {

	public static void main(String[] args) {
		SpringApplication.run(HowmeetApplication.class, args);
	}

}
