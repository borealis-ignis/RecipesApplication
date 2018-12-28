package com.recipes.appl.model.dto.errors;

/**
 * @author Kastalski Sergey
 */
public class StaticError implements StaticItemError {
	
	private String errorMessage;
	
	public StaticError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
}
