package com.volvo.congestiontax;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication
public class CongestionTaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(CongestionTaxApplication.class, args);
	}

}
