package com.payce.paymentgateway.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAutoConfiguration(exclude = LiquibaseAutoConfiguration.class)
public class AcqMockApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcqMockApplication.class, args);
	}

	@RestController
	public static class TestController {
		@PostMapping("/submit")
		public ResponseEntity<Void> post() {
			return ResponseEntity.ok()
					.build();
		}

		@GetMapping("/get")
		public ResponseEntity<String> get() {
			return ResponseEntity.ok()
					.body("Good one!");
		}
	}
}
