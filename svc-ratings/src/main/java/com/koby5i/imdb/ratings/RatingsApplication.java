package com.koby5i.imdb.ratings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RatingsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatingsApplication.class, args);
	}

}
