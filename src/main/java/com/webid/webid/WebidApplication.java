package com.webid.webid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebidApplication {

	public static void main(String[] args) {
		// Dotenv dotenv = Dotenv.load();

		// System.setProperty("DB_URL", dotenv.get("DB_URL"));
		// System.setProperty("DB_USER", dotenv.get("DB_USER"));
		// System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		// System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
		SpringApplication.run(WebidApplication.class, args);
	}

}
