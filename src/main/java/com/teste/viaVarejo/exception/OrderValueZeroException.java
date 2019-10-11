package com.teste.viaVarejo.exception;

public class OrderValueZeroException extends RuntimeException{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6033886413213096645L;

	public OrderValueZeroException(String message) {
		super(message);		
	}
	
	public OrderValueZeroException(String message, Throwable reason) {
		super(message, reason);
		
	}	
}
