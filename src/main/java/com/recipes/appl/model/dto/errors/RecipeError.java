package com.recipes.appl.model.dto.errors;

import com.recipes.appl.model.dto.RecipeDto;

/**
 * @author Kastalski Sergey
 */
public class RecipeError extends RecipeDto {
	
	private String errorMessage;
	
	public RecipeError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}
