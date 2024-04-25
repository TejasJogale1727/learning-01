package com.microservices.authenticationservice.dto;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@ToString
public class JWTResponse {

	private String jwtToken;
	private String username;
	
	public JWTResponse() {
		super();
	}
	
	public JWTResponse(String jwtToken, String username) {
		super();
		this.jwtToken = jwtToken;
		this.username = username;
	}
	
	public String getJwtToken() {
		return jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "JWTResponse [jwtToken=" + jwtToken + ", username=" + username + "]";
	}
}
