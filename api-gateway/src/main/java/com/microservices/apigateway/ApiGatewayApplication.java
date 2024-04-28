package com.microservices.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.core.publisher.Hooks;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
	    Hooks.enableAutomaticContextPropagation();
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
	
//	@Bean
//    public RestTemplate template(){
//       return new RestTemplate();
//    }
}
