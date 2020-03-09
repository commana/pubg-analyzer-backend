package de.techmastery.gaming.pubganalyzerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class PubgAnalyzerBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PubgAnalyzerBackendApplication.class, args);
	}

	@Bean
	public TaskExecutor taskExecutor() {
		return new ThreadPoolTaskExecutor();
	}

}
