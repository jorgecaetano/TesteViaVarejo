package com.teste.viaVarejo.exception;

public class InvalidOrderRequestException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2173292815089319260L;
	
	public InvalidOrderRequestException(String message) {
		super(message);		
	}
	
	public InvalidOrderRequestException(String message, Throwable reason) {
		super(message, reason);
		
	}	
}
