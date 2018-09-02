package com.recipes.appl.model.dto.errors;

import com.recipes.appl.model.dto.IngredientDto;

/**
 * @author Kastalski Sergey
 */
public class IngredientError extends IngredientDto {
	
	private String errorMessage;
	
	public IngredientError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}
