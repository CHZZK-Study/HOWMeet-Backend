package org.chzzk.howmeet.logging.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

@Configuration
public class LoggingConfig {
    @Bean
    public StopWatch stopWatch() {
        return new StopWatch();
    }
}
