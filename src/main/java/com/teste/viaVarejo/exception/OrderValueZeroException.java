package com.teste.viaVarejo.exception;

/**
 * Exception disparado para indicar que o valor do pedido será igual a zero
 * decorrente da diferença entre o valor do produto e o valor de entrada
 * 
 * @author Jorge Caetano
 */
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
