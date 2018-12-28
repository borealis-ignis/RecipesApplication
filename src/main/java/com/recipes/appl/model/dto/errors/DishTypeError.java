package com.recipes.appl.model.dto.errors;

import com.recipes.appl.model.dto.DishTypeDto;

/**
 * @author Kastalski Sergey
 */
public class DishTypeError extends DishTypeDto implements StaticItemError {
	
	private String errorMessage;
	
	public DishTypeError(final String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
