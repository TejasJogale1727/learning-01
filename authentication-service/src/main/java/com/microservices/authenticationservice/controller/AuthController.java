package com.microservices.authenticationservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
import com.microservices.authenticationservice.service.UserService;
import com.microservices.authenticationservice.util.JWTUtil;

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
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {

        this.doAuthenticate(request.getEmail(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

//        JWTResponse response = JWTResponse.builder()
//                .jwtToken(token)
//                .username(userDetails.getUsername()).build();
        
        JWTResponse response = new JWTResponse(token,userDetails.getUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
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
    public Boolean validateToken(@RequestParam(value = "token", required = false) String token) {
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(this.helper.getUsernameFromToken(token));
		Boolean validateToken = this.helper.validateToken(token, userDetails);
		return validateToken;
    }
}
