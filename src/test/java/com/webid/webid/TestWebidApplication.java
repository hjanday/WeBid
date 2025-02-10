package com.webid.webid;

import org.springframework.boot.SpringApplication;

public class TestWebidApplication {

	public static void main(String[] args) {
		SpringApplication.from(WebidApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
