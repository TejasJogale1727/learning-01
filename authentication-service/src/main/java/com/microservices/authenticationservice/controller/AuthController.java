package com.microservices.authenticationservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.authenticationservice.dto.JWTRequest;
import com.microservices.authenticationservice.dto.JWTResponse;
import com.microservices.authenticationservice.entities.User;
import com.microservices.authenticationservice.message.MessageStatus;
import com.microservices.authenticationservice.service.UserService;
import com.microservices.authenticationservice.util.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private UserService userService;

	@Autowired
	private JWTUtil helper;

	private Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	public ResponseEntity<MessageStatus<JWTResponse>> login(@RequestBody JWTRequest request) {
		MessageStatus<JWTResponse> msg = new MessageStatus<JWTResponse>();

		if(!this.doAuthenticate(request.getEmail(), request.getPassword())) {
			msg.setStatusCode(HttpStatus.NOT_FOUND);
			msg.setMessage("Email address not found in our records. Please provide correct Email Id.");
			return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
		String token = this.helper.generateToken(userDetails);

//        JWTResponse response = JWTResponse.builder()
//                .jwtToken(token)
//                .username(userDetails.getUsername()).build();

		JWTResponse response = new JWTResponse(token, userDetails.getUsername());
		msg.setStatusCode(HttpStatus.OK);
		msg.setData(response);
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

	private boolean doAuthenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			manager.authenticate(authentication);
			return true;
		} catch (BadCredentialsException | InternalAuthenticationServiceException e) {
//			throw new BadCredentialsException("Invalid Username or Password  !!");
			logger.error("Invalid Username or Password  !!!");
			logger.error(e.getMessage());
			return false;
		}

	}

	@ExceptionHandler(BadCredentialsException.class)
	public String exceptionHandler() {
		return "Credentials Invalid !!";
	}

	@PostMapping("/create-user")
	public User createUser(@RequestBody User user) {
		return userService.createUser(user);
	}

	@GetMapping("/validate-token")
	public Boolean validateToken(@RequestParam(value = "token", required = true) String token) {
		String username = null;
		Boolean validateToken = false;
		UserDetails userDetails = null;
		try {
			username = this.helper.getUsernameFromToken(token);
		} catch (SignatureException se) {
//			se.printStackTrace();
			logger.error("Invalid Token !!!");
			logger.error(se.getMessage());
		} catch (ExpiredJwtException ee) {
//			se.printStackTrace();
			logger.error("Token Expired !!!");
			logger.error(ee.getMessage());
		}
		if (username != null) {
			userDetails = this.userDetailsService.loadUserByUsername(username);
			if (userDetails != null) {
				logger.info("Valid token !!!");
				validateToken = this.helper.validateToken(token, userDetails);
			}
		}
		return validateToken;
	}
}
