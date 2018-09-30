package com.recipes.appl.exception;

/**
 * @author Kastalski Sergey
 */
public class RecipeException extends RuntimeException {
	
	private static final long serialVersionUID = -2606254870437627098L;
	
	public RecipeException() {
		super();
	}
	
	public RecipeException(final String message) {
		super(message);
	}
	
	public RecipeException(final String message, final Exception e) {
		super(message, e);
	}
	
}
