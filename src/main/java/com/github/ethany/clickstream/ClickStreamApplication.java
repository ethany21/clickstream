package com.github.ethany.clickstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ClickStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClickStreamApplication.class, args);
	}

}
