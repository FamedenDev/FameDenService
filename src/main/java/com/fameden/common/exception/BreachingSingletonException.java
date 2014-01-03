package com.fameden.common.exception;

public class BreachingSingletonException extends Exception {

	private static final long serialVersionUID = 1L;

	public BreachingSingletonException(String className) {
		super("Object Creation for "+className+" class is prohibited");
	}

}
