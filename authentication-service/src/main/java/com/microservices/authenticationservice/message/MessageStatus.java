package com.microservices.authenticationservice.message;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class MessageStatus<T> {
	
	@JsonInclude(Include.NON_NULL)
	private HttpStatus statusCode;
	
	@JsonInclude(Include.NON_NULL)
	private String message;
	
	@JsonInclude(Include.NON_NULL)
	private T data;

	public MessageStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public MessageStatus(String message, HttpStatus statusCode) {
		super();
		this.statusCode = statusCode;
		this.message = message;
	}
	
	
	public HttpStatus getStatusCode() {
		return statusCode;
	}


	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
}
