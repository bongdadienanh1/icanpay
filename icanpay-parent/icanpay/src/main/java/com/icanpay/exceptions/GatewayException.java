package com.icanpay.exceptions;

public class GatewayException extends RuntimeException {

	private static final long serialVersionUID = -4102205870968374470L;

	public GatewayException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}

	public GatewayException(String message, Throwable cause) {
		// TODO Auto-generated constructor stub
		super(message, cause);
	}

}
