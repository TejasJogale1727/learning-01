package com.microservices.apigateway.filter;

import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservices.apigateway.ApiGatewayApplication;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
	
	private Logger logger = LoggerFactory.getLogger(ApiGatewayApplication.class);
	
	@Autowired
	private RouteValidator validator;
	
    private WebClient webClient;

//    @Autowired
//    private RestTemplate template;

//    @Autowired
//    private JWTUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }
    
    @Autowired
    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.build();
    }

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			logger.info("Ensuring Request Security: Managing Request Authentication in the API Gateway Application");
			if (validator.isSecured.test(exchange.getRequest())) {
				// header contains token or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					ServerHttpResponse response = exchange.getResponse();
                	response.setStatusCode(HttpStatus.BAD_REQUEST);
                    String newResponseBody = "Missing authorization header";
                    DataBuffer dataBuffer = response.bufferFactory().wrap(newResponseBody.getBytes(StandardCharsets.UTF_8));
                    response.getHeaders().setContentLength(newResponseBody.length());
                    response.writeWith(Mono.just(dataBuffer)).subscribe();
                	exchange.mutate().response(response).build();
                	logger.error("Missing authorization header in request !!!");
                    return response.setComplete();
//                    throw new RuntimeException("missing authorization header");
				}

				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}
//				try {
					// REST call to AUTH service
//                	Boolean isTokenValidResponse = template.getForObject("http://AUTHENTICATION-SERVICE//validate-token?token=" + authHeader, Boolean.class);
//					template.getForObject("http://localhost:9090/auth/validate-token?token=" + authHeader, Boolean.class);
//					template.getForObject("http://AUTHENTICATION-SERVICE/auth/validate-token?token=" + authHeader, Boolean.class);
//                	template.getForObject("http://DEPARTMENT-SERVICE/department", String.class);
//                	template.getForObject("http://authentication-service/auth/validate-token?token=" + authHeader, Boolean.class);
//                	ResponseEntity<Boolean> response = template.exchange("http://AUTHENTICATION-SERVICE/auth/validate-token?token=" + authHeader, HttpMethod.GET,null,Boolean.class);
//                	Boolean isTokenValidResponse = response.getBody();
//                	if (!isTokenValidResponse) {
//                		throw new RuntimeException("un authorized access to application");
//					}
					
					String authServiceUrl = "http://AUTHENTICATION-SERVICE";

		            return this.webClient
		                    .get()
		                    .uri(authServiceUrl + "/auth/validate-token?token="+ authHeader)
		                    .retrieve()
		                    .bodyToMono(Boolean.class)
		                    .flatMap(response -> {
		                        // Do further operations on microservice response if needed
		                    	if (!response) {
		                    		throw new RuntimeException("un authorized access to application");
								}
		                    	logger.info("User authenticated successfully !!!, Forwarding request : "+exchange.getRequest().getURI().getHost()+" to respective service application.");
		                        return chain.filter(exchange);
		                    })
		                    .onErrorResume(throwable -> {
//		                        throw new RuntimeException("un authorized access to application");
//		                    	exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // Any status code can be set here.

		                    	ServerHttpResponse response = exchange.getResponse();
		                    	response.setStatusCode(HttpStatus.UNAUTHORIZED);
//		                        String newResponseBody =
//		                                "<body>\n" +
//		                                "      <h1 style=\"color:red;text-align:center\">Bad Request </h1>\n" +
//		                                "      <p>If you are seeing this page it means response body is modified.</p>\n" +
//		                                "  </body>";
		                        String newResponseBody = "Unauthorized access to application";
		                        DataBuffer dataBuffer = response.bufferFactory().wrap(newResponseBody.getBytes(StandardCharsets.UTF_8));
		                        response.getHeaders().setContentLength(newResponseBody.length());
		                        response.writeWith(Mono.just(dataBuffer)).subscribe();
		                    	exchange.mutate().response(response).build();
		                    	logger.error("Unauthorized access to application !!!, Token provided might be invalid or expired.");
		                        return response.setComplete();
		                    });
					
//				} catch (Exception e) {
//					System.out.println("invalid access...!");
//					throw new RuntimeException("un authorized access to application");
//				}
			}
			logger.info("User authentication did not required for provided request, Forwarding request : "+exchange.getRequest().getURI().getHost()+" to respective service application");
			return chain.filter(exchange);
		});
	}

	public static class Config {

	}
}
