package com.common.retryProcess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RetryProcessApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetryProcessApplication.class, args);
	}

}
