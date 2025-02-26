package com.webid.webid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WebidApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebidApplication.class, args);
	}

}

	